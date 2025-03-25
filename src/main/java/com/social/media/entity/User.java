package com.social.media.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document (collection="users")
public class User  {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String firstname;
    private String lastname;
    private String profilePicture;
    private String coverPicture;
    private String about;
    private String livesIn;
    private String worksAt;
    private String gmail;
    private String relationship;
    private String country;
    private Set<String> followers; // List of follower IDs
    private Set<String> following;
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private Set<ObjectId> likedPosts;
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<ObjectId> posts;
    private List<String> roles;


}
