package com.danilogalvao.workshopmongo.resources;

import com.danilogalvao.workshopmongo.domain.User;
import com.danilogalvao.workshopmongo.dto.UserDTO;
import com.danilogalvao.workshopmongo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity <Void> insert(@RequestBody UserDTO objDto) {
        User user = userServ.fromDTO(objDto);
        user = userServ.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity <Void> delete(@PathVariable String id) {
        userServ.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/{id}", method= RequestMethod.PUT)
    public ResponseEntity <Void> update(@RequestBody UserDTO objDto, @PathVariable String id) {
        User user = userServ.fromDTO(objDto);
        user.setId(id);
        user = userServ.update(user);
        return ResponseEntity.noContent().build();
    }
}
