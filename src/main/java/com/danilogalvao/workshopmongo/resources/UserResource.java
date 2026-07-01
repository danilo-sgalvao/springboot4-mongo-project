package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity <List<User>> findAll() {
        User u1 = new User("1", "Maria", "maria@gmail");
        User u2 = new User("2", "Joao", "joao@gmail");
        List<User> list = new ArrayList<>(Arrays.asList(u1, u2));
        return ResponseEntity.ok().body(list);
    }
}
