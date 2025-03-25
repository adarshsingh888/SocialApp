package com.social.media.service;

import com.social.media.entity.User;
import com.social.media.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Disabled
    @Test
    public void testAdd(){
        assertTrue(5>3);
    }
 //   @Disabled
    @ParameterizedTest
    @CsvSource({
            "adarsh_singh",
            "john"
    })
    public void findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        assertTrue(!user.getPosts().isEmpty());

    }
   // @Disabled
    @ParameterizedTest
    @CsvSource({
            "6,3,3",
             "3,2,2"
    })
    public  void test(int exc,int a,int b){
         assertEquals(exc,a+b);
    }

}
