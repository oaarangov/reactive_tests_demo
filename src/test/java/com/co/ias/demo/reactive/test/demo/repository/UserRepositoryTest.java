package com.co.ias.demo.reactive.test.demo.repository;

import com.co.ias.demo.reactive.test.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void listUsersTest() {
        User user1 = User.builder()
                .id(1)
                .name("Pepe")
                .email("pepe@mail.com")
                .build();
        User user2 = User.builder()
                .id(2)
                .name("Luis")
                .email("luis@mail.com")
                .build();
        Flux<User> users = Flux.just(user1, user2);

        Publisher<User> setup = repository.findAll().thenMany(users);

        StepVerifier
                .create(setup)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getUserTest() {
        String name = "Pepe";

        Publisher<User> setup = repository.findByName(name);

        StepVerifier
                .create(setup)
                .consumeNextWith(user -> {
                    assertEquals(user.getName(), name);
                })
                .verifyComplete();
    }

    @Test
    void getUserByEmailTest() {
        String email = "pepe@mail.com";

        Publisher<User> setup = repository.findByEmail(email);

        StepVerifier
                .create(setup)
                .consumeNextWith(user -> {
                    assertEquals(user.getEmail(), email);
                })
                .verifyComplete();
    }

    @Test
    void createUserTest() {
        User user1 = User.builder()
                .name("Pepe")
                .email("pepe@mail.com")
                .build();

        Publisher<User> setup = repository.deleteAll().then(repository.save(user1));
        Mono<User> find = repository.findByName("Pepe");

        Publisher<User> composite = Mono.from(setup).then(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(user -> {
                    assertEquals(user.getName(), "Pepe");
                })
                .verifyComplete();
    }
}