package com.example.doantotnghiep.model.responeadmin;

import com.example.doantotnghiep.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse {

    private String user;

    private String content;

    public CommentResponse(Comment comment) {
        this.user = comment.getUser().getFullName();
        this.content = comment.getContent();
    }
}
