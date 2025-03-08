package com.social.media.repository;

import com.social.media.entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
   public List<Post> findByUserId(ObjectId userId);
}
