package com.danilogalvao.workshopmongo.services;

import com.danilogalvao.workshopmongo.domain.User;
import com.danilogalvao.workshopmongo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }
    public List<User> findAll(){
        return userRepo.findAll();
    }
}
