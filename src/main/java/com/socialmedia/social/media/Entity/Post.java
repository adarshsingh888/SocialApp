package com.socialmedia.social.media.Entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Date;
@Data
@Document(collection = "posts")
public class Post {
    @Id
    private ObjectId postId;
    private ObjectId userId;
    private String des;
    private ArrayList<ObjectId> likes;
    private Date createdAt;
    private String image;


}

