package com.example.doantotnghiep.controller.shop;

import com.example.doantotnghiep.entity.Post;
import com.example.doantotnghiep.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@CrossOrigin("*")
public class BlogController {

    @Autowired
    private PostService postService;

    @GetMapping("/tin-tuc")
    public ResponseEntity<Object> getPostPages(Model model,
                               @RequestParam(defaultValue = "1", required = false) Integer page) {

        Page<Post> posts = postService.getListPost(page);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("currentPage", posts.getPageable().getPageNumber() + 1);
        return ResponseEntity.ok(model);
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
