package com.danilogalvao.workshopmongo.services;

import com.danilogalvao.workshopmongo.domain.Post;
import com.danilogalvao.workshopmongo.repository.PostRepository;
import com.danilogalvao.workshopmongo.services.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepo;

    public PostService(PostRepository postRepository) {
        this.postRepo = postRepository;
    }

    public Post findById(String id){
        Optional<Post> post = postRepo.findById(id);
        return post.orElseThrow(() -> new ObjectNotFoundException("Post not found"));
    }

    public List<Post> findByTitle(String title){
        return postRepo.findByTitleContainingIgnoreCase(title);
    }
}
