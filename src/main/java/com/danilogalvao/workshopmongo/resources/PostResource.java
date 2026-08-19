package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.Post;
import com.danilogalvao.workshopmongo.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posts")
public class PostResource {

    private final PostService postServ;

    public PostResource(PostService postServ){
        this.postServ = postServ;
    }
    
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public ResponseEntity <Post> findById(@PathVariable String id) {
        Post post = postServ.findById(id);
        return ResponseEntity.ok().body(post);
    }
}
