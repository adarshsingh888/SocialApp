package com.SocialMedia.Social.Media.Controller;

import com.SocialMedia.Social.Media.Entity.User;
import com.SocialMedia.Social.Media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if(users.isEmpty()) return new ResponseEntity<>("NO users found . Please add new user then try again", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(users, HttpStatus.OK); // 200 OK
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> createaAdmin(@PathVariable String username){
        User user=userService.findByUsername(username);
        if(user == null){
            return new ResponseEntity<>("Username did not exits",HttpStatus.NOT_FOUND);}
        user.getRoles().add("ADMIN");
        userService.save(user);
        return new ResponseEntity<>("ADMIN status granted",HttpStatus.CREATED);
    }

    @PutMapping("/admin-remove/{username}")
    public ResponseEntity<?> removeAdmin(@PathVariable String username){
        User user=userService.findByUsername(username);
        if(user == null){
            return new ResponseEntity<>("Username did not exits",HttpStatus.NOT_FOUND);
        }
        user.getRoles().remove("ADMIN");
        userService.save(user);
        return new ResponseEntity<>("ADMIN status removed",HttpStatus.CREATED);
    }
}
