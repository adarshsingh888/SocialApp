package com.social.media.dto;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class UserUpdateDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String profilePicture;
    private String coverPicture;
    private String about;
    private String livesIn;
    private String worksAt;
    private String gmail;
    private String relationship;
    private String country;
}