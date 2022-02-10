package com.hallos.blogapp.controller;

import com.hallos.blogapp.dto.CommentDto;
import com.hallos.blogapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @Valid @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getAllCommentsByPostId(postId));
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentByPostId(@PathVariable Long postId, @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentByPostId(postId, commentId));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentByPostId(@PathVariable Long postId,
                                                            @PathVariable Long commentId,
                                                            @Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateCommentByPostId(postId, commentId, commentDto));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentByPostId(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteCommentByPostId(postId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
