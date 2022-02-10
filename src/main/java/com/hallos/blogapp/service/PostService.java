package com.hallos.blogapp.service;

import com.hallos.blogapp.dto.PostDto;
import com.hallos.blogapp.dto.PostResponse;
import com.hallos.blogapp.exception.ResourceNotFoundException;
import com.hallos.blogapp.model.Post;
import com.hallos.blogapp.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostService(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public PostDto createPost(PostDto postDto) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .content(postDto.getContent())
                .build();
        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    public PostResponse getAllPosts(int pageSize, int pageNo, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content = listOfPosts.stream().map(p -> modelMapper.map(p, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    public PostDto getPostById(Long id) {
        return modelMapper.map(postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id)), PostDto.class);
    }

    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return modelMapper.map(updatedPost, PostDto.class);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}
