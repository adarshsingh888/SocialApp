package com.social.media;

import com.social.media.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.AccessType;
@Disabled
@SpringBootTest
class SocialMediaApplicationTests {
    @Autowired
	private UserService userService;

}
