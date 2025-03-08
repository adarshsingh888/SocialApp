package com.socialmedia.social.media.service;

import com.socialmedia.social.media.Entity.User;
import com.socialmedia.social.media.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

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
        if(user.getGmail() != null){
        emailService.sendEmail(user.getGmail(),"Alert !","Your Social media account data is edited");}
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

    public String followUnfollow(ObjectId userId,String username) {
        User user=userRepository.findByUsername(username);
        Optional<User> followingUser=userRepository.findById(userId);
        User user2=followingUser.get();
        if(user == null) return null;
        if(user.getFollowing()== null) user.setFollowing(new HashSet<>());
        if(user2.getFollowers()== null) user2.setFollowers(new HashSet<>());
        if(user.getFollowing().contains(userId)){
            user.getFollowing().remove(userId);
            user2.getFollowers().remove(user.getId());
            userRepository.save(user);
            userRepository.save(user2);
            return "Unfollowed the User";
        }
        else{
            user.getFollowing().add(userId);
            user2.getFollowers().add(user.getId());
            userRepository.save(user);
            userRepository.save(user2);
            return "User followed";
        }

    }

    public List<User> unFollowedUser(String userName){
        User user=userRepository.findByUsername(userName);
        if(user.getFollowing()==null) return null;
        return userRepository.findUnfollowedUsers(user.getFollowing(),user.getId());
    }

    public List<User> followedUser(String userName) {
        User user=userRepository.findByUsername(userName);
        if(user.getFollowing()==null) return null;
        return userRepository.findFollowedUsers(user.getFollowing(),user.getId());
    }
}
