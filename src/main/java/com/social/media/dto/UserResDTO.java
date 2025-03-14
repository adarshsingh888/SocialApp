package com.social.media.dto;

import com.social.media.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResDTO {
    private String token;
    private User user;
}
