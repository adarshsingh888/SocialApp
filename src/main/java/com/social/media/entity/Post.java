package com.social.media.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId postId;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId userId;
    private String des;
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private ArrayList<ObjectId> likes;
    private LocalDateTime createdAt;
    private String image;


}

