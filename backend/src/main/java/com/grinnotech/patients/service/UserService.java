package com.grinnotech.patients.service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;
import com.grinnotech.patients.config.profiles.mongodb.MongoDb;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.OrganizationRepository;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dao.authorities.RequireAdminAuthority;
import com.grinnotech.patients.model.*;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;
import com.grinnotech.patients.util.ValidationUtil;
import com.mongodb.client.model.Filters;
import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.util.*;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.Collections.singleton;
import static java.util.Date.from;
import static java.util.stream.Collectors.toSet;

@Service
@RequireAdminAuthority
public class UserService extends AbstractService<Patient> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Validator validator;

    @Autowired
    private MongoDb mongoDb;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<User> read(ExtDirectStoreReadRequest request) {

        StringFilter filter = request.getFirstFilterForField("filter");
        List<User> list = (filter != null)
                ? userRepository.findAllWithFilterActive(filter.getValue(), getSpringSort(request))
                : userRepository.findAllActive(getSpringSort(request));

        list.forEach(this::loadOrganizations);

        logger.debug("read size:[{}]", list.size());

        return new ExtDirectStoreResult<>(list);
    }

    private void loadOrganizations(User user) {
        user.setOrganizations(new HashSet<>((Collection<Organization>) organizationRepository.findAll(user.getOrganizationIds())));
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<User> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, User user) {
        ExtDirectStoreResult<User> result = new ExtDirectStoreResult<>();
        if (isLastAdmin(user.getId())) {
            return result.setSuccess(false);
        }

        logger.debug("destroy 1");
        User old = userRepository.findOne(user.getId());

        old.setId(null);
        old.setActive(false);
        userRepository.save(old);
        logger.debug("destroy 2 " + old.getId());

        setAttrsForDelete(user, userDetails, old);
        userRepository.save(user);
        deletePersistentLogins(user.getId());

        logger.debug("destroy end");

        return result.setSuccess(true);
    }

    private void deletePersistentLogins(String userId) {
        mongoDb.getCollection(PersistentLogin.class).deleteMany(Filters.eq(CPersistentLogin.userId, userId));
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<User> update(@AuthenticationPrincipal MongoUserDetails userDetails, User user) {
//        List<ValidationMessages> violations = validateEntity(user, locale);
//        violations.addAll(checkIfLastAdmin(user, locale));
//
//        if (violations.isEmpty()) {
//            List<Bson> updates = new ArrayList<>();
//            updates.add(Updates.set(CUser.email, user.getEmail()));
//            updates.add(Updates.set(CUser.firstName, user.getFirstName()));
//            updates.add(Updates.set(CUser.lastName, user.getLastName()));
//            updates.add(Updates.set(CUser.locale, user.getLocale()));
//            updates.add(Updates.set(CUser.enabled, user.isEnabled()));
//            if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
//                updates.add(Updates.set(CUser.authorities, user.getAuthorities()));
//            } else {
//                updates.add(Updates.unset(CUser.authorities));
//            }
//            updates.add(Updates.setOnInsert(CUser.deleted, false));
//
//            UpdateResult result = mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, user.getId()), Updates.combine(updates),
//                    new UpdateOptions().upsert(true));
//
//            if (!user.isEnabled()) {
//                deletePersistentLogins(user.getId());
//            }
//
//            return new ValidationMessagesResult<>(user);
//        }
//
//        ValidationMessagesResult<User> result = new ValidationMessagesResult<>(user);
//        result.setValidations(violations);
//        return result;

        List<ValidationMessages> violations = validateEntity(user, userDetails.getLocale());
        violations.addAll(checkIfLastAdmin(user, userDetails.getLocale()));

        ValidationMessagesResult<User> result = new ValidationMessagesResult<>(user);
        result.setValidations(violations);

        logger.debug("update 1: " + user.toString());
        if (violations.isEmpty()) {
            User old = userRepository.findOne(user.getId());

            if (old != null) {
                old.setId(null);
                old.setActive(false);
                setAttrsForUpdate(user, userDetails, old);
            } else {
                setAttrsForCreate(user, userDetails);
            }

            if (user.getOrganizations() == null || user.getOrganizations().isEmpty()) {
                Organization organization = organizationRepository.findByCodeActive("PPMDPoland"); // TODO: change to user active org
                user.setOrganizationIds(singleton(organization.getId()));
                loadOrganizations(user);
            } else {
                user.setOrganizationIds(user.getOrganizations().stream().map(Organization::getId).collect(toSet()));
            }

            // copy all JsonIgnore fields
            if (old != null) {
                user.setPasswordHash(old.getPasswordHash());
                user.setPasswordResetToken(old.getPasswordResetToken());
                user.setPasswordResetTokenValidUntil(old.getPasswordResetTokenValidUntil());
                user.setSecret(old.getSecret());
            }
            userRepository.save(user);
            if (!user.isEnabled()) {
                deletePersistentLogins(user.getId());
            }

            if (old != null)
                userRepository.save(old);
        }

        logger.debug("update end");
        return result;
    }

    private List<ValidationMessages> checkIfLastAdmin(User user, Locale locale) {
        User dbUser = userRepository.findOne(user.getId());

        List<ValidationMessages> validationErrors = new ArrayList<>();

        if (dbUser != null && (!user.isEnabled() || user.getAuthorities() == null || !user.getAuthorities().contains(Authority.ADMIN.name()))) {
            if (isLastAdmin(user.getId())) {

                ObjectDiffer objectDiffer = ObjectDifferBuilder.startBuilding().filtering().returnNodesWithState(State.UNTOUCHED).and().build();
                DiffNode diff = objectDiffer.compare(user, dbUser);

                DiffNode diffNode = diff.getChild(CUser.enabled);
                if (!diffNode.isUntouched()) {
                    user.setEnabled(dbUser.isEnabled());

                    ValidationMessages validationError = new ValidationMessages();
                    validationError.setField(CUser.enabled);
                    validationError.setMessage(messageSource.getMessage("user_lastadmin_error", null, locale));
                    validationErrors.add(validationError);
                }

                diffNode = diff.getChild(CUser.authorities);
                if (!diffNode.isUntouched()) {
                    user.setAuthorities(dbUser.getAuthorities());

                    ValidationMessages validationError = new ValidationMessages();
                    validationError.setField(CUser.authorities);
                    validationError.setMessage(messageSource.getMessage("user_lastadmin_error", null, locale));
                    validationErrors.add(validationError);
                }
            }
        }

        return validationErrors;
    }

    private List<ValidationMessages> validateEntity(User user, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, user);

        String c = user.getId();
        boolean b = !isEmailUnique(user.getId(), user.getEmail());
        if (!isEmailUnique(user.getId(), user.getEmail())) {
            ValidationMessages validationError = new ValidationMessages();
            validationError.setField(CUser.email);
            validationError.setMessage(messageSource.getMessage("user_emailtaken", null, locale));
            validations.add(validationError);
        }

        return validations;
    }

    private boolean isLastAdmin(String id) {
        return userRepository.existsByIdAndAuthoritiesActive(id, singleton(Authority.ADMIN.name()));
    }

    private boolean isEmailUnique(String id, String email) {
        return id != null ?
                !userRepository.existsByIdNotAndEmailActive(id, email)
                :
                !userRepository.existsByEmailActive(email);
    }

    @ExtDirectMethod
    public void unlock(String userId) {
        User user = userRepository.findOne(userId);
        user.setLockedOutUntil(null);
        user.setFailedLogins(0);
        userRepository.save(user);
    }

    @ExtDirectMethod
    public void disableTwoFactorAuth(String userId) {
        User user = userRepository.findOne(userId);
        user.setSecret(null);
        userRepository.save(user);
    }

    @ExtDirectMethod
    public void sendPassordResetEmail(String userId) {
        String token = UUID.randomUUID().toString();

        User user = userRepository.findOne(userId);
        user.setPasswordResetTokenValidUntil(from(now(UTC).plusHours(4).toInstant()));
        user.setPasswordResetToken(token);
        userRepository.save(user);

        mailService.sendPasswortResetEmail(user);
    }

}
