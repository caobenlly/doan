package com.example.doantotnghiep.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateCommentPostRequest {
    private long userId;
    @NotBlank(message = "Nội dung trống!")
    private String content;
}
