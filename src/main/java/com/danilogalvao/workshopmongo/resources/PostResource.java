package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.Post;
import com.danilogalvao.workshopmongo.resources.util.URL;
import com.danilogalvao.workshopmongo.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value="/titlesearch", method= RequestMethod.GET)
    public ResponseEntity <List<Post>> findByTitle(@RequestParam(value="text", defaultValue = "") String text) {
        text = URL.decodeParam(text);
        List<Post> list = postServ.findByTitle(text);
        return ResponseEntity.ok().body(list);
    }
}
