package com.example.doantotnghiep.service;

import com.example.doantotnghiep.entity.Post;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.model.dto.PageableDTO;
import com.example.doantotnghiep.model.request.CreatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    PageableDTO adminGetListPost(String title, String status, int page);

    Post createPost(CreatePostRequest createPostRequest, User user);

    void updatePost(CreatePostRequest createPostRequest, User user, Long id);

    void deletePost(long id);

    Post getPostById(long id);

    List<Post> adminGetListPosts(String title, String status);

    List<Post> getLatesPost();

    List<Post> getListPost(int page);

    List<Post> getLatestPostsNotId(long id);

    long getCountPost();
}
