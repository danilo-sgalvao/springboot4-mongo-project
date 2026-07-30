package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.User;
import com.danilogalvao.workshopmongo.dto.UserDTO;
import com.danilogalvao.workshopmongo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    private final UserService userServ;

    public UserResource(UserService userServ){
        this.userServ = userServ;
    }

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity <List<UserDTO>> findAll() {
        List<User> list = userServ.findAll();
        List<UserDTO> listDTO = list.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listDTO);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public ResponseEntity <UserDTO> findById(@PathVariable String id) {
        User user = userServ.findById(id);
        return ResponseEntity.ok().body(new UserDTO(user));
    }
}
