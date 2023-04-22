package com.example.doantotnghiep.model.dto;

import com.example.doantotnghiep.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private long id;

    private String slug;

    private String title;

    private String thumbnail;

    private Timestamp createdAt;

    private Timestamp publishedAt;
    private Timestamp modifiedAt;
    private int status;

    public PostDTO(Post p) {
        this.id = p.getId();
        this.slug = p.getSlug();
        this.title = p.getTitle();
        this.thumbnail = p.getThumbnail();
        this.createdAt = p.getCreatedAt();
        this.status = p.getStatus();
        this.modifiedAt = p.getModifiedAt();
    }


}
