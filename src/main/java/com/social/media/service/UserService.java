package com.social.media.service;

import com.social.media.dto.UserUpdateDTO;
import com.social.media.entity.User;
import com.social.media.repository.PostRepository;
import com.social.media.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private EmailService emailService;
    @Autowired
    private PostRepository postRepository;
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

    public ResponseEntity<User> updateUser(String userName, UserUpdateDTO updatedUserDTO) {
        // Find the existing user
        User user = userRepository.findByUsername(userName);
        if (user == null) {
           return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Check if the new username is already taken by another user
        User existingUser = userRepository.findByUsername(updatedUserDTO.getUsername());
        if (existingUser != null && !existingUser.getUsername().equals(userName)) {
           return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }

        // Update the user's fields
        user.setUsername(updatedUserDTO.getUsername());
        user.setFirstname(updatedUserDTO.getFirstname());
        user.setLastname(updatedUserDTO.getLastname());
        user.setProfilePicture(updatedUserDTO.getProfilePicture());
        user.setCoverPicture(updatedUserDTO.getCoverPicture());
        user.setAbout(updatedUserDTO.getAbout());
        user.setLivesIn(updatedUserDTO.getLivesIn());
        user.setWorksAt(updatedUserDTO.getWorksAt());
        user.setGmail(updatedUserDTO.getGmail());
        user.setRelationship(updatedUserDTO.getRelationship());
        user.setCountry(updatedUserDTO.getCountry());

        // Encode password if it's updated
        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
        }

        // Save the updated user
        User savedUser = userRepository.save(user);
        // Send email notification only after successful update
        if (savedUser.getGmail() != null) {
//            emailService.sendEmail(savedUser.getGmail(), "Alert!", "Your Social media account data has been updated.");
        }
        return new ResponseEntity<>(savedUser,HttpStatus.OK);
    }

    public String deleteUser(String userName) {
        // Find user by username
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            return "User not found!";
        }

        // Remove user from followers' following list (if followers exist)
        if (user.getFollowers() != null) {
            List<User> followers = new ArrayList<>();
            for (String username : user.getFollowers()) {
                User follower = userRepository.findByUsername(username);
                if (follower != null && follower.getFollowing() != null) {
                    follower.getFollowing().remove(userName);
                    followers.add(follower);
                }
            }
            if (!followers.isEmpty()) {
                userRepository.saveAll(followers); // Save all updated follower users
            }
        }

        // Remove user from following's followers list (if following exists)
        if (user.getFollowing() != null) {
            List<User> following = new ArrayList<>();
            for (String username : user.getFollowing()) {
                User followedUser = userRepository.findByUsername(username);
                if (followedUser != null && followedUser.getFollowers() != null) {
                    followedUser.getFollowers().remove(userName);
                    following.add(followedUser);
                }
            }
            if (!following.isEmpty()) {
                userRepository.saveAll(following); // Save all updated following users
            }
        }

        // Delete user's posts (if any exist)
        if (user.getPosts() != null && !user.getPosts().isEmpty()) {
            postRepository.deleteAllById(user.getPosts());
        }

        // Finally, delete the user
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

    public User followUnfollow(String userName,String username) {
        User Mainuser=userRepository.findByUsername(userName);
        User userToFollowed=userRepository.findByUsername(username);
        if(Mainuser == null) return null;
        if(Mainuser.getFollowing()== null) Mainuser.setFollowing(new HashSet<>());
        if(userToFollowed.getFollowers()== null) userToFollowed.setFollowers(new HashSet<>());
        if(Mainuser.getFollowing().contains(username)){
            Mainuser.getFollowing().remove(username);
            userToFollowed.getFollowers().remove(userName);
            userRepository.save(Mainuser);
            userRepository.save(userToFollowed);
            return Mainuser;
        }
        else{
            Mainuser.getFollowing().add(username);
            userToFollowed.getFollowers().add(userName);
            userRepository.save(Mainuser);
            userRepository.save(userToFollowed);
            return Mainuser;
        }

    }

    public List<User> unFollowedUser(String userName){
        User user=userRepository.findByUsername(userName);
        if(user.getFollowing()==null) user.setFollowing(new HashSet<>());
        return userRepository.findUnfollowedUsers(user.getFollowing(),user.getId());
    }

    public List<User> followedUser(String userName) {
        User user=userRepository.findByUsername(userName);
        if(user.getFollowing()==null) return null;
        return userRepository.findFollowedUsers(user.getFollowing(),user.getId());
    }
}
