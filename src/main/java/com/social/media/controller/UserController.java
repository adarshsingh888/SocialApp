package com.social.media.controller;

import com.social.media.apiresponse.WeatherResponse;
import com.social.media.dto.UserUpdateDTO;
import com.social.media.entity.User;
import com.social.media.service.EmailService;
import com.social.media.service.UserService;

import com.social.media.service.WeatherServices;
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
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private  UserService userService;
    @Autowired
    private WeatherServices weatherServices;

//    @Autowired
//    private EmailService emailService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if(user == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK); // 200 OK
    }


    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return  userService.updateUser(userName, userUpdateDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user=userService.findByUsername(userName);
        if(user.getGmail() != null) {
//            emailService.sendEmail(
//                    user.getGmail(), user.getFirstname()+", your account is deleted.",
//                    user.getUsername()+" account is deleted.");
        }
        String message = userService.deleteUser(userName);
        if(message== null) return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(message, HttpStatus.OK); // 200 OK
    }
    @GetMapping("/weather/{place}")
    public ResponseEntity<WeatherResponse> getWeather(@PathVariable String place){
        return new ResponseEntity<>(weatherServices.getWeather(place),HttpStatus.OK);
    }
    @PutMapping("/{username}")
    public User followUnfollow(@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userService.followUnfollow(userName,username);
    }

    @GetMapping("/unFollowedUser")
    public ResponseEntity<List<User>> unFollowedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<User> users=userService.unFollowedUser(userName);
        //System.out.println(users);
        if(users == null || users.isEmpty()) return new ResponseEntity<>(null,HttpStatus.OK);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    @GetMapping("/followedUser")
    public ResponseEntity<List<User>> followedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<User> users=userService.followedUser(userName);
        if(users == null || users.isEmpty()) return new ResponseEntity<>(null,HttpStatus.OK);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
