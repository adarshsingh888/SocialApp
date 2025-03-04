package com.SocialMedia.Social.Media.Entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document (collection="users")
public class User  {

    @Id
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String firstname;
    private String lastname;
    private boolean isAdmin = false; // Default value
    private String profilePicture;
    private String coverPicture;
    private String about;
    private String livesIn;
    private String worksAt;
    private String relationship;
    private String country;
    private List<String> followers; // List of follower IDs
    private List<String> following;
    private List<ObjectId> posts;
    private List<String> roles;



}
