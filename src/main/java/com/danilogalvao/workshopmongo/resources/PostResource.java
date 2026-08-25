package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.Post;
import com.danilogalvao.workshopmongo.resources.util.URL;
import com.danilogalvao.workshopmongo.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @RequestMapping(value="/fullsearch", method= RequestMethod.GET)
    public ResponseEntity <List<Post>> fullSearch(
            @RequestParam(value="text", defaultValue = "") String text,
            @RequestParam(value="minDate", defaultValue = "") String minDateParam,
            @RequestParam(value="maxDate", defaultValue = "") String maxDateParam) {

        text = URL.decodeParam(text);
        LocalDateTime minDate = URL.parseDate(minDateParam, LocalDateTime.of(1970, 1, 1, 0, 0));
        LocalDateTime maxDate = URL.parseDate(maxDateParam, LocalDateTime.now());
        List<Post> list = postServ.fullSearch(text, minDate, maxDate);
        return ResponseEntity.ok().body(list);
    }
}
