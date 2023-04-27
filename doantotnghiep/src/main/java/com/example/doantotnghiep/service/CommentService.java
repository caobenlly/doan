package com.example.doantotnghiep.service;


import com.example.doantotnghiep.entity.Comment;
import com.example.doantotnghiep.model.request.CreateCommentPostRequest;
import com.example.doantotnghiep.model.request.CreateCommentProductRequest;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest, String id);
    Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, Long userId);
}
