package com.grinnotech.patients.mongodb.dao;


import com.grinnotech.patients.mongodb.model.User;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Jacek Sztajnke
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

	@Query("{ active: true }")
	List<User> findAllActive(Sort sort);

	@Query("{$and: [{ $or: [{lastName: {$regex:?0,$options:'i'}}, {firstName: {$regex:?0,$options:'i'}} ]}, { active: true } ]}")
	List<User> findAllWithFilterActive(String filter, Sort sort);

	@Query("{ $and : [ { email: ?0 }, { active: true } ] }")
	Optional<User> findByEmailActive(String email);

	@Query("{ $and : [ { passwordResetToken: ?0 }, { enabled: true }, { active: true } ] }")
	User findOneByPasswordResetTokenAndEnabled(String passwordResetToken);

	boolean existsByEmailRegexAndIdNot(String email, String id);

	@ExistsQuery("{ $and : [ { id: ?0 }, { authorities: ?0 }, { enabled: true }, { active: true } ] }")
	boolean existsByIdAndAuthoritiesActive(String id, Set<String> authorities);

	@ExistsQuery("{ $and : " +
			"[ { id: { $ne: ?0 }}, { email: {$regex:?0,$options:'i'} }, { enabled: true }, { active: true } ] }")
	boolean existsByIdNotAndEmailActive(String id, String email);

	@ExistsQuery("{ $and : " +
			"[ { email: {$regex:?0,$options:'i'} }, { enabled: true }, { active: true } ] }")
	boolean existsByEmailActive(String email);
}
