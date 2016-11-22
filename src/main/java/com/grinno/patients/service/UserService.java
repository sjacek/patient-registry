package com.grinno.patients.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Validator;

import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;
import com.grinno.patients.config.MongoDb;
import com.grinno.patients.dao.authorities.RequireAdminAuthority;
import com.grinno.patients.model.Authority;
import com.grinno.patients.model.CPersistentLogin;
import com.grinno.patients.model.CUser;
import com.grinno.patients.model.PersistentLogin;
import com.grinno.patients.model.User;
import com.grinno.patients.util.QueryUtil;
import com.grinno.patients.util.ValidationMessages;
import com.grinno.patients.util.ValidationMessagesResult;
import com.grinno.patients.util.ValidationUtil;
import com.mongodb.client.result.UpdateResult;
import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.State;

@Service
@RequireAdminAuthority
public class UserService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Validator validator;

    @Autowired
    private MongoDb mongoDb;

    @Autowired
    private MailService mailService;

//    @Autowired
//    public UserService(MongoDb mongoDb, Validator validator, MessageSource messageSource, MailService mailService) {
//        this.mongoDb = mongoDb;
//        this.messageSource = messageSource;
//        this.validator = validator;
//        this.mailService = mailService;
//    }

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<User> read(ExtDirectStoreReadRequest request) {

        List<Bson> andFilters = new ArrayList<>();
        StringFilter filter = request.getFirstFilterForField("filter");
        if (filter != null) {
            List<Bson> orFilters = new ArrayList<>();
            orFilters.add(Filters.regex(CUser.lastName, filter.getValue(), "i"));
            orFilters.add(Filters.regex(CUser.firstName, filter.getValue(), "i"));
            orFilters.add(Filters.regex(CUser.email, filter.getValue(), "i"));

            andFilters.add(Filters.or(orFilters));
        }
        andFilters.add(Filters.eq(CUser.deleted, false));

        long total = mongoDb.getCollection(User.class).count(Filters.and(andFilters));

        FindIterable<User> find = mongoDb.getCollection(User.class).find(Filters.and(andFilters));
        find.sort(Sorts.orderBy(QueryUtil.getSorts(request)));
        find.skip(request.getStart());
        find.limit(request.getLimit());

        return new ExtDirectStoreResult<>(total, QueryUtil.toList(find));
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<User> destroy(User user) {
        ExtDirectStoreResult<User> result = new ExtDirectStoreResult<>();
        if (!isLastAdmin(user.getId())) {
            mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, user.getId()), Updates.set(CUser.deleted, true));
            result.setSuccess(Boolean.TRUE);

            deletePersistentLogins(user.getId());
        } else {
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }

    private void deletePersistentLogins(String userId) {
        mongoDb.getCollection(PersistentLogin.class).deleteMany(Filters.eq(CPersistentLogin.userId, userId));
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<User> update(User user, Locale locale) {
        List<ValidationMessages> violations = validateEntity(user, locale);
        violations.addAll(checkIfLastAdmin(user, locale));

        if (violations.isEmpty()) {
            List<Bson> updates = new ArrayList<>();
            updates.add(Updates.set(CUser.email, user.getEmail()));
            updates.add(Updates.set(CUser.firstName, user.getFirstName()));
            updates.add(Updates.set(CUser.lastName, user.getLastName()));
            updates.add(Updates.set(CUser.locale, user.getLocale()));
            updates.add(Updates.set(CUser.enabled, user.isEnabled()));
            if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
                updates.add(Updates.set(CUser.authorities, user.getAuthorities()));
            } else {
                updates.add(Updates.unset(CUser.authorities));
            }
            updates.add(Updates.setOnInsert(CUser.deleted, false));

            UpdateResult result = mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, user.getId()), Updates.combine(updates),
                    new UpdateOptions().upsert(true));

            if (!user.isEnabled()) {
                deletePersistentLogins(user.getId());
            }

            return new ValidationMessagesResult<>(user);
        }

        ValidationMessagesResult<User> result = new ValidationMessagesResult<>(user);
        result.setValidations(violations);
        return result;
    }

    private List<ValidationMessages> checkIfLastAdmin(User user, Locale locale) {
        User dbUser = mongoDb.getCollection(User.class).find(Filters.eq(CUser.id, user.getId())).first();

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
        List<ValidationMessages> validations = ValidationUtil.validateEntity(this.validator, user);

        if (!isEmailUnique(user.getId(), user.getEmail())) {
            ValidationMessages validationError = new ValidationMessages();
            validationError.setField(CUser.email);
            validationError.setMessage(messageSource.getMessage("user_emailtaken", null, locale));
            validations.add(validationError);
        }

        return validations;
    }

    private boolean isLastAdmin(String id) {

        long count = mongoDb.getCollection(User.class)
                .count(Filters.and(Filters.ne(CUser.id, id),
                        Filters.eq(CUser.deleted, false),
                        Filters.eq(CUser.authorities, Authority.ADMIN.name()),
                        Filters.eq(CUser.enabled, true)));
        return count == 0;
    }

    private boolean isEmailUnique(String userId, String email) {
        if (StringUtils.hasText(email)) {
            long count;
            if (userId != null) {
                count = mongoDb.getCollection(User.class).count(Filters.and(
                        Filters.eq(CUser.deleted, false),
                        Filters.regex(CUser.email, "^" + email + "$", "i"),
                        Filters.ne(CUser.id, userId)));
            } else {
                count = mongoDb.getCollection(User.class).count(Filters.regex(CUser.email, "^" + email + "$", "i"));
            }

            return count == 0;
        }

        return true;
    }

    @ExtDirectMethod
    public void unlock(String userId) {
        mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, userId),
                Updates.combine(Updates.unset(CUser.lockedOutUntil), Updates.set(CUser.failedLogins, 0)));
    }

    @ExtDirectMethod
    public void disableTwoFactorAuth(String userId) {
        mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, userId), Updates.unset(CUser.secret));
    }

    @ExtDirectMethod
    public void sendPassordResetEmail(String userId) {
        String token = UUID.randomUUID().toString();

        User user = mongoDb.getCollection(User.class).findOneAndUpdate(
                Filters.eq(CUser.id, userId),
                Updates.combine(Updates.set(CUser.passwordResetTokenValidUntil,
                        Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusHours(4).toInstant())),
                        Updates.set(CUser.passwordResetToken, token)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));

        this.mailService.sendPasswortResetEmail(user);
    }

}
