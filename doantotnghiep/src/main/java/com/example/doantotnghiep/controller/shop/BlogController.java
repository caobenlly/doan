package com.example.doantotnghiep.controller.shop;

import com.example.doantotnghiep.entity.Post;
import com.example.doantotnghiep.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BlogController {

    @Autowired
    private PostService postService;

    @GetMapping("/tin-tuc")
    public ResponseEntity<Object> getPostPages(Model model,
                               @RequestParam(defaultValue = "1", required = false) Integer page) {

        List<Post> posts = postService.getListPost(page);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tin-tuc/{slug}/{id}")
    public ResponseEntity<Object> getPostDetail(Model model, @PathVariable long id){
        Post post = postService.getPostById(id);
        List<Post> postList = postService.getLatestPostsNotId(id);
        model.addAttribute("post",post);
        model.addAttribute("postList",postList);

        return ResponseEntity.ok(model);
    }

}
