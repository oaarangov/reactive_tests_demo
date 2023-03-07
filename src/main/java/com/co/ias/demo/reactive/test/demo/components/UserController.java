package com.co.ias.demo.reactive.test.demo.components;

import com.co.ias.demo.reactive.test.demo.model.User;
import com.co.ias.demo.reactive.test.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<Flux<User>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
