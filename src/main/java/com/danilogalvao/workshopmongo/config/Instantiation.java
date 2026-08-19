package com.danilogalvao.workshopmongo.config;

import com.danilogalvao.workshopmongo.domain.Post;
import com.danilogalvao.workshopmongo.domain.User;
import com.danilogalvao.workshopmongo.dto.AuthorDTO;
import com.danilogalvao.workshopmongo.repository.PostRepository;
import com.danilogalvao.workshopmongo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Configuration
public class Instantiation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Instantiation(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();
        postRepository.deleteAll();

        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        userRepository.saveAll(Arrays.asList(maria, alex, bob));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Post post1 = new Post(null, LocalDate.parse("21/03/2018", formatter), "partiu viagem", "vou viajar para sao paulo", new AuthorDTO(maria));
        Post post2 = new Post(null, LocalDate.parse("23/03/2018", formatter), "bom dia", "acordei feliz", new AuthorDTO(maria));

        postRepository.saveAll(Arrays.asList(post1, post2));

        maria.getPosts().addAll(Arrays.asList(post1, post2));
        userRepository.save(maria);

    }
}
