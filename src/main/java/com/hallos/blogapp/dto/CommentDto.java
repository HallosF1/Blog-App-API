package com.hallos.blogapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @NotEmpty(message = "Name should not be empty")
    @Email
    private String email;

    @NotEmpty
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String body;
}
