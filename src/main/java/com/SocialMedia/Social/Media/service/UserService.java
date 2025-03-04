package com.SocialMedia.Social.Media.service;

import com.SocialMedia.Social.Media.Entity.User;
import com.SocialMedia.Social.Media.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();  // Inject PasswordEncoder

//    private static final Logger logger= LoggerFactory.getLogger(UserService.class);  // after using annotation @Slf4j we dont did this line

    public User addnewUser(User user) {
     try{
         String encodedPassword = passwordEncoder.encode(user.getPassword());
         user.setPassword(encodedPassword);
         user.setRoles(List.of("USER"));
         return userRepository.save(user);
         //throw new Exception("this logging testing");
     }catch(Exception e){
         log.error("error for username {}",user.getUsername());
//         log.info("sdfasasdsafsfs");
//         log.debug("sdfasasdsafsfs");
//         log.warn("sdfasasdsafsfs");
//         log.trace("sdfasasdsafsfs");
         return null;
     }
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User updateUser(String userName, User updatedUser) {
        String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
        updatedUser.setPassword(encodedPassword);
        updatedUser.setRoles(Arrays.asList("USER"));
        User user=userRepository.findByUsername(userName);
        if(userRepository.findByUsername(updatedUser.getUsername()) != null &&
                !userRepository.findByUsername(updatedUser.getUsername()).getUsername().equals(userName)) return null;
        updatedUser.setId(user.getId());
        return userRepository.save(updatedUser);
    }

    public String deleteUser(String userName) {
        User user=userRepository.findByUsername(userName);
        if (user == null) {
            return null;
        }
        userRepository.deleteById(user.getId());
        return "User deleted successfully!";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(ObjectId userId) {
        return userRepository.findById(userId);
    }

    public void save(User u) {
        userRepository.save(u);
    }
}
