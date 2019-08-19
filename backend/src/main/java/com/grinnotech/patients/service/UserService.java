package com.grinnotech.patients.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patients.model.Authority.ADMIN;
import static com.grinnotech.patients.util.OptionalEx.ifPresent;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;
import static com.grinnotech.patients.util.ThrowingFunction.sneakyThrow;
import static de.danielbechler.diff.node.DiffNode.State.UNTOUCHED;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.Collections.singleton;
import static java.util.Date.from;
import static java.util.stream.Collectors.toSet;

import com.grinnotech.patients.NotFoundException;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.OrganizationRepository;
import com.grinnotech.patients.dao.PersistentLoginRepository;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dao.authorities.RequireAdminAuthority;
//import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.Organization;
import com.grinnotech.patients.model.User;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Validator;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;
import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;

/**
 * @author Jacek Sztajnke
 */
@Service
@RequireAdminAuthority
public class UserService extends AbstractService<User> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final MessageSource messageSource;

	private final Validator validator;

	private final UserRepository userRepository;

	private final MailService mailService;

	private final OrganizationRepository organizationRepository;

	private final PersistentLoginRepository persistentLoginRepository;

	@Autowired
	public UserService(MessageSource messageSource, Validator validator, UserRepository userRepository,
			MailService mailService, OrganizationRepository organizationRepository,
			PersistentLoginRepository persistentLoginRepository) {
		this.messageSource = messageSource;
		this.validator = validator;
		this.userRepository = userRepository;
		this.mailService = mailService;
		this.organizationRepository = organizationRepository;
		this.persistentLoginRepository = persistentLoginRepository;
	}

	@ExtDirectMethod(STORE_READ)
	public ExtDirectStoreResult<User> read(ExtDirectStoreReadRequest request) {

		StringFilter filter = request.getFirstFilterForField("filter");
		List<User> list = (filter != null) ?
				userRepository.findAllWithFilterActive(filter.getValue(), getSpringSort(request)) :
				userRepository.findAllActive(getSpringSort(request));

		userRepository.loadOrganizationsData(list);

		logger.debug("read size:[{}]", list.size());

		return new ExtDirectStoreResult<>(list);
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ExtDirectStoreResult<User> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, @NotNull User user)
			throws NotFoundException {
		ExtDirectStoreResult<User> result = new ExtDirectStoreResult<>();
		if (isLastAdmin(user.getId())) {
			return result.setSuccess(false);
		}

		logger.debug("destroy 1");
		Optional<User> oOld = userRepository.findById(user.getId());
		User old = oOld.orElseThrow(() -> new NotFoundException("User id={} not found", user.getId()));
		old.setId(null);
		old.setActive(false);
		userRepository.save(old);
		logger.debug("destroy 2 " + old.getId());

		setAttrsForDelete(user, userDetails, old);
		userRepository.save(user);
		persistentLoginRepository.deleteByUserId(user.getId());

		logger.debug("destroy end");

		return result.setSuccess(true);
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ValidationMessagesResult<User> update(@NotNull @AuthenticationPrincipal MongoUserDetails userDetails, User user) {
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
			Optional<User> old = userRepository.findById(user.getId());
			ifPresent(old, user1 -> {
				user1.setId(null);
				user1.setActive(false);
				try {
					setAttrsForUpdate(user, userDetails, user1);
				} catch (NotFoundException e) {
					sneakyThrow(e);
				}
			}).orElse(() -> {
				try {
					setAttrsForCreate(user, userDetails);
				} catch (NotFoundException e) {
					sneakyThrow(e);
				}
			});

			if (user.getOrganizations() == null || user.getOrganizations().isEmpty()) {
				Organization organization = organizationRepository
						.findByCodeActive("PPMDPoland"); // TODO: change to user active org
				user.setOrganizationIds(singleton(organization.getId()));
				userRepository.loadOrganizationsData(user);
			} else {
				user.setOrganizationIds(user.getOrganizations().stream().map(Organization::getId).collect(toSet()));
			}

			// copy all JsonIgnore fields
			old.ifPresent(user1 -> {
				user.setPasswordHash(user1.getPasswordHash());
				user.setPasswordResetToken(user1.getPasswordResetToken());
				user.setPasswordResetTokenValidUntil(user1.getPasswordResetTokenValidUntil());
				user.setSecret(user1.getSecret());
			});
			userRepository.save(user);
			if (!user.isEnabled())
				persistentLoginRepository.deleteByUserId(user.getId());

			old.ifPresent(userRepository::save);
		}

		logger.debug("update end");
		return result;
	}

	@NotNull
	private List<ValidationMessages> checkIfLastAdmin(@NotNull User user, Locale locale) {
		Optional<User> dbUser = userRepository.findById(user.getId());

		List<ValidationMessages> validationErrors = new ArrayList<>();

		if (dbUser.isPresent() && (!user.isEnabled() || user.getAuthorities() == null || !user.getAuthorities()
				.contains(ADMIN.name()))) {
			if (isLastAdmin(user.getId())) {

				ObjectDiffer objectDiffer = ObjectDifferBuilder.startBuilding().filtering()
						.returnNodesWithState(UNTOUCHED).and().build();
				DiffNode diff = objectDiffer.compare(user, dbUser);

//				User user1 = dbUser.get();
//				DiffNode diffNode = diff.getChild(CUser.enabled);
//				if (!diffNode.isUntouched()) {
//					user.setEnabled(user1.isEnabled());
//
//					validationErrors.add(ValidationMessages.builder().field(CUser.enabled)
//							.message(messageSource.getMessage("user_lastadmin_error", null, locale)).build());
//				}

//				diffNode = diff.getChild(CUser.authorities);
//				if (!diffNode.isUntouched()) {
//					user.setAuthorities(user1.getAuthorities());
//
//					validationErrors.add(ValidationMessages.builder().field(CUser.authorities)
//							.message(messageSource.getMessage("user_lastadmin_error", null, locale)).build());
//				}
			}
		}

		return validationErrors;
	}

	private List<ValidationMessages> validateEntity(User user, Locale locale) {
		List<ValidationMessages> validations = super.validateEntity(user);

		if (!isEmailUnique(user.getId(), user.getEmail())) {
//			validations.add(ValidationMessages.builder().field(CUser.email)
//					.message(messageSource.getMessage("user_emailtaken", null, locale)).build());
		}

		return validations;
	}

	private boolean isLastAdmin(String id) {
		return userRepository.existsByIdAndAuthoritiesActive(id, singleton(ADMIN.name()));
	}

	private boolean isEmailUnique(String id, String email) {
		return id != null ?
				!userRepository.existsByIdNotAndEmailActive(id, email) :
				!userRepository.existsByEmailActive(email);
	}

	@ExtDirectMethod
	public void unlock(String userId) throws NotFoundException {
		Optional<User> oUser = userRepository.findById(userId);
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userId));
		user.setLockedOutUntil(null);
		user.setFailedLogins(0);
		userRepository.save(user);
	}

	@ExtDirectMethod
	public void disableTwoFactorAuth(String userId) throws NotFoundException {
		Optional<User> oUser = userRepository.findById(userId);
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userId));
		user.setSecret(null);
		userRepository.save(user);
	}

	@ExtDirectMethod
	public void sendPassordResetEmail(String userId) throws NotFoundException {
		String token = UUID.randomUUID().toString();

		Optional<User> oUser = userRepository.findById(userId);
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userId));
		user.setPasswordResetTokenValidUntil(from(now(UTC).plusHours(4).toInstant()));
		user.setPasswordResetToken(token);
		userRepository.save(user);

		mailService.sendPasswortResetEmail(user);
	}

}
