package com.social.media.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private ObjectId postId;
    private ObjectId userId;
    private String des;
    private ArrayList<ObjectId> likes;
    private LocalDateTime createdAt;
    private String image;


}

