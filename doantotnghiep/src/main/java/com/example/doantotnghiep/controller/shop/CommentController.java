package com.example.doantotnghiep.controller.shop;

import com.example.doantotnghiep.entity.Comment;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.model.request.CreateCommentPostRequest;
import com.example.doantotnghiep.model.request.CreateCommentProductRequest;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comments/post/{id}")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CreateCommentPostRequest createCommentPostRequest , @PathVariable String id) {
//        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Comment comment = commentService.createCommentPost(createCommentPostRequest,id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/api/comments/product")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CreateCommentProductRequest createCommentProductRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Comment comment = commentService.createCommentProduct(createCommentProductRequest, user.getId());
        return ResponseEntity.ok(comment);
    }
}
