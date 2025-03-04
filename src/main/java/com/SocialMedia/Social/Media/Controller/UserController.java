package com.SocialMedia.Social.Media.Controller;

import com.SocialMedia.Social.Media.Entity.User;
import com.SocialMedia.Social.Media.service.UserService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private  UserService userService;

    // Constructor Injection (No need for @Autowired)

    // Add a new user


    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()){
            return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK); // 200 OK
    }

    // Update user by ID
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User updated = userService.updateUser(userName, updatedUser);
        if (updated == null) return new ResponseEntity<>("UserName already exits", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    // Delete user by ID
    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String message = userService.deleteUser(userName);
        if(message== null) return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(message, HttpStatus.OK); // 200 OK
    }

}
