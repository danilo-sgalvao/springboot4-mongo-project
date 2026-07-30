package com.danilogalvao.workshopmongo.services;

import com.danilogalvao.workshopmongo.domain.User;
import com.danilogalvao.workshopmongo.repository.UserRepository;
import com.danilogalvao.workshopmongo.services.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public  User findById(String id){
        Optional<User> user = userRepo.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }
}
