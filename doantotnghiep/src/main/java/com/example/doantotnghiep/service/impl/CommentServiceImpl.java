package com.example.doantotnghiep.service.impl;

import com.example.doantotnghiep.entity.Comment;
import com.example.doantotnghiep.entity.Post;
import com.example.doantotnghiep.entity.Product;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.entity.exception.InternalServerException;
import com.example.doantotnghiep.model.request.CreateCommentPostRequest;
import com.example.doantotnghiep.model.request.CreateCommentProductRequest;
import com.example.doantotnghiep.responsitory.CommentRepository;
import com.example.doantotnghiep.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    EntityManager entityManager;
    @Override
    public Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest, String id ) {

//        Post post = new Post();
//
//        post.setId(createCommentPostRequest.getPostId());
        Comment comment = new Comment();

//        comment.setPost(post);
        comment.setContent(createCommentPostRequest.getContent());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Product product = new Product();
        product.setId(id);
        comment.setProduct(product);
        User user = new User();
        user.setId(createCommentPostRequest.getUserId());
        comment.setUser(user);
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new InternalServerException("Có lỗi trong quá trình bình luận!");
        }
        return comment;
    }

    @Override
    public Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, Long userId) {
        Comment comment = new Comment();
        comment.setContent(createCommentProductRequest.getContent());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        User user = new User();
        user.setId(userId);
        comment.setUser(user);
        Product product = new Product();
        product.setId(createCommentProductRequest.getProductId());
        comment.setProduct(product);
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new InternalServerException("Có lỗi trong quá trình bình luận!");
        }
        return comment;
    }
}
