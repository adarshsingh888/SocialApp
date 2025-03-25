package com.social.media.controller;

import com.social.media.dto.UserResDTO;
import com.social.media.entity.User;
import com.social.media.dto.LoginDTO;
import com.social.media.dto.UserDTO;
import com.social.media.service.UserDetailsImpl;
import com.social.media.service.UserService;
import com.social.media.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public PublicController(UserService userService, AuthenticationManager authenticationManager, UserDetailsImpl userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/health-check")
    public String  hello(){
        return "hello world";
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResDTO> signUp(@RequestBody UserDTO userDTO) {
        if(userService.findByUsername(userDTO.getUsername()) != null) {
            return new ResponseEntity<>( HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        User newUser=userService.addnewUser(user);
        if(newUser == null) return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        LoginDTO loginDTO=new LoginDTO();
        loginDTO.setPassword(userDTO.getPassword());
        loginDTO.setUsername(userDTO.getUsername());
        return logIn(loginDTO); // 201 Created
    }
    @PostMapping("/login")
    public ResponseEntity<UserResDTO> logIn(@RequestBody LoginDTO loginDTO){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            User user=userService.findByUsername(loginDTO.getUsername());
            UserResDTO userResDTO=new UserResDTO();
            userResDTO.setToken(jwt);
            userResDTO.setUser(user);

            return new ResponseEntity<>(userResDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }



}
