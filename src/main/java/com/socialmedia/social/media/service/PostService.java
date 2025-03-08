package com.socialmedia.social.media.service;

import com.socialmedia.social.media.Entity.Post;
import com.socialmedia.social.media.Entity.User;
import com.socialmedia.social.media.repository.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private  PostRepository postRepository;
    @Autowired
     private  UserService userService;

    @Transactional
    public Post createpost(Post post,String username){
        try{
            User user= userService.findByUsername(username);
            if(user.getPosts() == null) user.setPosts(new ArrayList<>());
            post.setUserId(user.getId());
            postRepository.save(post);
            user.getPosts().add(post.getPostId());
            userService.saveUser(user);
            return post;

        }catch (Exception e){
            System.out.println(e);
        }


        return post;
    }
    public List<Post> getallPost(){
        return postRepository.findAll();
    }
    public Post getPostById(ObjectId postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null); // Returns Post if present, else null
    }

    @Transactional
    public boolean deletepostbyId(ObjectId postId,String username) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            User user = userService.findByUsername(username);
            Optional<User> uu= userService.findById(post.get().getUserId());
            if(!uu.get().getUsername() .equals(username) ){
                return false;
            }
            user.getPosts().removeIf( x -> x.equals(postId));
            userService.save(user);
            postRepository.deleteById(postId);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public ResponseEntity<?> likeDislikePost(ObjectId postId, String userName) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            System.out.println("Post not found");
            return new ResponseEntity<>("Post did not found",HttpStatus.NOT_FOUND);
        }
        Post post = optionalPost.get();
        if (post.getLikes() == null) {
            post.setLikes(new ArrayList<>());
        }
        User user=userService.findByUsername(userName);
        String aa=null;
        if (!post.getLikes().contains(user.getId())) {
            aa="Post Liked";
            post.getLikes().add(user.getId());
        }else {
            aa="Post Disliked";
            post.getLikes().remove(user.getId());
        }
        postRepository.save(post);
        return new ResponseEntity<>(aa,HttpStatus.OK);

    }


    public ResponseEntity<?> updatePostById(ObjectId postId, Post post, String userName) {
        Optional<Post> opPost=postRepository.findById(postId);
        User user=userService.findByUsername(userName);
        Post p=opPost.get();
        if(!user.getId().equals(p.getUserId())){
            return new ResponseEntity<>("You are not allowed to edit this post",HttpStatus.CONFLICT);
        }
        post.setPostId(p.getPostId());
        post.setUserId(p.getUserId());
        postRepository.save(post);
        return new ResponseEntity<>("Updated successfully",HttpStatus.OK);
    }


    public List<Post> getPostsofUserId(ObjectId userId){
        List<Post> posts=postRepository.findByUserId(userId);
        return posts;
    }
}
