package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.User;
import com.danilogalvao.workshopmongo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    private final UserService userServ;

    public UserResource(UserService userServ){
        this.userServ = userServ;
    }

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity <List<User>> findAll() {
        List<User> list = userServ.findAll();
        return ResponseEntity.ok().body(list);
    }
}
