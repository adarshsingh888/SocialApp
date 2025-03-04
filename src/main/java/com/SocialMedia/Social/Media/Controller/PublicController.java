package com.SocialMedia.Social.Media.Controller;

import com.SocialMedia.Social.Media.Entity.User;
import com.SocialMedia.Social.Media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String  hello(){
        return "hello world";
    }

    @PostMapping("/adduser")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {

        if(userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exits! Please try another username.", HttpStatus.CONFLICT);
        }
        User savedUser = userService.addnewUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 201 Created
    }



}
