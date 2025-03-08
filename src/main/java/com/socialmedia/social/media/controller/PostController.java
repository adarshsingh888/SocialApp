package com.socialmedia.social.media.controller;

import com.socialmedia.social.media.Entity.Post;
import com.socialmedia.social.media.service.PostService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return new ResponseEntity(postService.createpost(post,userName), HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getallPost(){
        List<Post> post=postService.getallPost();
        if(post.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post,HttpStatus.OK) ;
    }
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable ObjectId postId){
        Post post=postService.getPostById(postId);
        if(post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post,HttpStatus.OK) ;
    }
    @PutMapping("/update/{postId}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId postId,@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return postService.updatePostById(postId,post,userName);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if(!postService.deletepostbyId(postId,userName)){
            return new ResponseEntity<>("Post does not exits.",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Post deleted successfully",HttpStatus.OK);

    }
    @PutMapping("/like/{postId}")
    public ResponseEntity<?> likeDislikePost( @PathVariable ObjectId postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return  postService.likeDislikePost(postId,userName);
    }

    @GetMapping("/userpost/{userId}")
    public ResponseEntity<?> getPostsofUserId(@PathVariable ObjectId userId){
        List<Post>posts=postService.getPostsofUserId(userId);

        if(posts.size() == 0){
            return new ResponseEntity<>("No post found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
}
