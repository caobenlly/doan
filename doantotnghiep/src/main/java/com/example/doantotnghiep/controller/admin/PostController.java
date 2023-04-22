package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.entity.Post;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.model.dto.PostDTO;
import com.example.doantotnghiep.model.request.CreatePostRequest;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.service.ImageService;
import com.example.doantotnghiep.service.PostService;
import org.hibernate.event.spi.PostDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/admin/posts")
    public ResponseEntity<Object> getPostManagePage(Model model) {


        List<PostDTO> result = postService.adminGetListPosts();



        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin/posts/create")
    public ResponseEntity<Object> getPostCreatePage(Model model) {
        // Get list image of user
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<String> images = imageService.getListImageOfUser(user.getId());
        model.addAttribute("images", images);

        return ResponseEntity.ok(model);
    }

    @GetMapping("/api/admin/posts")
    public ResponseEntity<Object> getListPosts() {
        List<PostDTO> posts = postService.adminGetListPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/api/admin/posts")
    public ResponseEntity<Object> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Post post = postService.createPost(createPostRequest, user);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/admin/posts/{slug}/{id}")
    public ResponseEntity<Object> getPostDetailPage(Model model, @PathVariable long id) {
        // Get list image of user
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<String> images = imageService.getListImageOfUser(user.getId());
        model.addAttribute("images", images);

        Post post = postService.getPostById(id);
        model.addAttribute("post", post);

        return ResponseEntity.ok(model);
    }

    @PutMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody CreatePostRequest createPostRequest, @PathVariable long id) {
        User user = ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUser();
        postService.updatePost(createPostRequest, user, id);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
