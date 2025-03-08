package com.social.media.repository;

import com.social.media.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
    @Query("{ $and: [{'_id': {$nin: ?0}}, {'_id': {$ne: ?1}}] }")
    List<User> findUnfollowedUsers(Set<ObjectId> following, ObjectId userId);
    @Query("{ $and:[{'_id':{$in:?0}},{'_id': {$ne: ?1}}]}")
    List<User> findFollowedUsers(Set<ObjectId> following, ObjectId id);
}
