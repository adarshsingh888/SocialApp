package com.social.media.repository;

import com.social.media.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, ObjectId> {
   User findByUsername(String username);
    @Query("{ $and: [{'username': {$nin: ?0}}, {'_id': {$ne: ?1}}] }")
    List<User> findUnfollowedUsers(Set<String> following, ObjectId userId);
    @Query("{ $and:[{'username':{$in:?0}},{'_id': {$ne: ?1}}]}")
    List<User> findFollowedUsers(Set<String> following, ObjectId id);





}
