package com.socialmedia.social.media.repository;

import com.socialmedia.social.media.Entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
   public List<Post> findByUserId(ObjectId userId);
}
