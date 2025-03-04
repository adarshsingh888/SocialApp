package com.SocialMedia.Social.Media.repository;

import com.SocialMedia.Social.Media.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {


    User findByUsername(String username);
}
