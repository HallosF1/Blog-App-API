package com.hallos.blogapp.service;

import com.hallos.blogapp.dto.CommentDto;
import com.hallos.blogapp.exception.BlogAPIException;
import com.hallos.blogapp.exception.ResourceNotFoundException;
import com.hallos.blogapp.model.Comment;
import com.hallos.blogapp.model.Post;
import com.hallos.blogapp.repository.CommentRepository;
import com.hallos.blogapp.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = Comment.builder()
                .name(commentDto.getName())
                .email(commentDto.getEmail())
                .body(commentDto.getBody())
                .post(post)
                .build();
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }

    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId).stream().map(c -> modelMapper.map(c, CommentDto.class)).collect(Collectors.toList());
    }

    public CommentDto getCommentByPostId(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return modelMapper.map(comment, CommentDto.class);
    }

    public CommentDto updateCommentByPostId(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setBody(commentDto.getBody());
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }

    public void deleteCommentByPostId(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        commentRepository.deleteById(commentId);
    }
}
