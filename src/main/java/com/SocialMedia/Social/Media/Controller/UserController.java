package com.SocialMedia.Social.Media.Controller;

import com.SocialMedia.Social.Media.Entity.User;
import com.SocialMedia.Social.Media.service.EmailService;
import com.SocialMedia.Social.Media.service.UserService;

import com.SocialMedia.Social.Media.service.WeatherServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private  UserService userService;
    @Autowired
    private WeatherServices weatherServices;

    @Autowired
    private EmailService emailService;

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

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        emailService.sendEmail(
                "singh273013@gmail.com","you  account is deleted",
                "this is informe that your  social media account is deleted");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String message = userService.deleteUser(userName);
        if(message== null) return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(message, HttpStatus.OK); // 200 OK
    }
    @GetMapping("/weather/{place}")
    public ResponseEntity<?> getWeather(@PathVariable String place){
        return new ResponseEntity<>(weatherServices.getWeather(place),HttpStatus.OK);
    }
    @PostMapping("/{userId}")
    public String followUnfollow(@PathVariable ObjectId userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userService.followUnfollow(userId,userName);
    }

    @GetMapping("/unfolloweduser")
    public ResponseEntity<?> unFollowedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<User> users=userService.unFollowedUser(userName);
        if(users == null || users.isEmpty()) return new ResponseEntity<>("Did not follow any User.",HttpStatus.OK);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    @GetMapping("/followeduser")
    public ResponseEntity<?> followedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<User> users=userService.followedUser(userName);
        if(users == null || users.isEmpty()) return new ResponseEntity<>("Did not follow any User.",HttpStatus.OK);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
