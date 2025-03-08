package com.SocialMedia.Social.Media.Controller;

import com.SocialMedia.Social.Media.Entity.User;
import com.SocialMedia.Social.Media.service.UserDetailsImpl;
import com.SocialMedia.Social.Media.service.UserService;
import com.SocialMedia.Social.Media.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/health-check")
    public String  hello(){
        return "hello world";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if(userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exits! Please try another username.", HttpStatus.CONFLICT);
        }
        User savedUser = userService.addnewUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 201 Created
    }
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody User user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }



}
