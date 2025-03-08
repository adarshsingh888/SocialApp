package com.socialmedia.social.media.service;

import com.socialmedia.social.media.Entity.Post;
import com.socialmedia.social.media.Entity.User;
import com.socialmedia.social.media.dto.PostDTO;
import com.socialmedia.social.media.repository.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
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
            Post newPost=  postRepository.save(post);
            user.getPosts().add(newPost.getPostId());
            userService.saveUser(user);
            return post;

        }catch (Exception e){
            System.out.println(e);
        }


        return post;
    }
    public List<Post> getAllPost(){
        return postRepository.findAll();
    }
    public Post getPostById(ObjectId postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null); // Returns Post if present, else null
    }

    @Transactional
    public ResponseEntity<String> deletePostById(ObjectId postId,String username) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            User user = userService.findByUsername(username);
            if(post.isPresent() && !post.get().getUserId() .equals(user.getId()) ){
                return new ResponseEntity<>("You are not allowed to delete this Post",HttpStatus.CONFLICT);
            }
            user.getPosts().removeIf( x -> x.equals(postId));
            userService.save(user);
            postRepository.deleteById(postId);
            return new ResponseEntity<>("Post deleted successfully",HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong .",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> likeDislikePost(ObjectId postId, String userName) {
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
        if(user.getLikedPosts() == null) user.setLikedPosts(new HashSet<>());
        String msg=null;
        if (!post.getLikes().contains(user.getId())) {
            msg="Post Liked";
            post.getLikes().add(user.getId());
            user.getLikedPosts().add(post.getPostId());
        }else {
            msg="Post Disliked";
            post.getLikes().remove(user.getId());
            user.getLikedPosts().remove(post.getPostId());
        }
        postRepository.save(post);
        userService.save(user);
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }


    public ResponseEntity<String> updatePostById(ObjectId postId, PostDTO postDTO, String userName) {
        Optional<Post> opPost=postRepository.findById(postId);
        User user=userService.findByUsername(userName);
        Post post=null;
        if(opPost.isPresent())  post=opPost.get();
        else if(post == null) return  new ResponseEntity<>("Post did not found.",HttpStatus.NOT_FOUND);
        if(!user.getId().equals(post.getUserId())){
            return new ResponseEntity<>("You are not allowed to edit this post",HttpStatus.CONFLICT);
        }
        post.setImage(postDTO.getImage());
        post.setDes(postDTO.getDes());
        postRepository.save(post);
        return new ResponseEntity<>("Updated successfully",HttpStatus.OK);
    }

    public List<Post> getPostsOfUserId(ObjectId userId){
        return postRepository.findByUserId(userId);
    }
}
