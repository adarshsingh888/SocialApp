package com.social.media.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.social.media.entity.Post;
import com.social.media.dto.PostDTO;
import com.social.media.entity.User;
import com.social.media.service.PostService;
import com.social.media.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final UserService userService;
    private final PostService postService;
    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Post post=new Post();
        post.setDes(postDTO.getDes());
        post.setImage(postDTO.getImage());
        post.setCreatedAt(LocalDateTime.now());
        return new ResponseEntity<>(postService.createpost(post,userName), HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Post>> getAllPost(){
        List<Post> post=postService.getAllPost();
        if(post.isEmpty()) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post,HttpStatus.OK) ;
    }
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable ObjectId postId){
        Post post=postService.getPostById(postId);
        if(post == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post,HttpStatus.OK) ;
    }
    @PutMapping("/{postId}")
    public ResponseEntity<String> updateById(@PathVariable ObjectId postId,@RequestBody PostDTO postDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return postService.updatePostById(postId,postDTO,userName);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<String > deleteById(@PathVariable ObjectId postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return postService.deletePostById(postId,userName);
    }
    @PutMapping("/like/{postId}")
    public ResponseEntity<String> likeDislikePost( @PathVariable ObjectId postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return  postService.likeDislikePost(postId,userName);
    }

    @GetMapping("/user-post/{userId}")
    public ResponseEntity<List<Post>> getPostsOfUserId(@PathVariable ObjectId userId){
        List<Post>posts=postService.getPostsOfUserId(userId);
        if(posts == null || posts.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/userPostByUsername/{username}")
    public ResponseEntity<List<Post>> getPostOfUserName(@PathVariable String username){
        User user=userService.findByUsername(username);
      return new ResponseEntity<>(postService.findByUserId(user.getId()),HttpStatus.OK)  ;

    }
}
