package com.co.ias.demo.reactive.test.demo.handlers;

import com.co.ias.demo.reactive.test.demo.components.UserRouter;
import com.co.ias.demo.reactive.test.demo.model.User;
import com.co.ias.demo.reactive.test.demo.repository.UserRepository;


import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserRouter.class, UserHandler.class})
@WebFluxTest
public class UserHandlerTest {
    @MockBean
    private UserRepository repository;
    @Autowired
    private WebTestClient testClient;

    @Test
    void listUserTest() {
        User user1 = User.builder()
                .id(1)
                .name("Pepe")
                .email("pepe@gmail.com")
                .build();
        User user2 = User.builder()
                .id(2)
                .name("Luis")
                .email("luis@gmail.com")
                .build();

        when(repository.findAll()).thenReturn(Flux.just(user1, user2));

        testClient.get()
                .uri("/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(User.class)
                .value(user -> {
                    assertThat(user.get(0).getName()).isEqualTo("Pepe");
                    assertThat(user.get(1).getName()).isEqualTo("Luis");
                });
    }

    @Test
    void getUserTest() {
        User user1 = User.builder()
                .id(1)
                .name("Pepe")
                .email("pepe@gmail.com")
                .build();
        Mono<User> monoUser = Mono.just(user1);

        when(repository.findById(user1.getId())).thenReturn(monoUser);

        testClient.get()
                .uri("/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(User.class)
                .value(user -> {
                    assertThat(user.getName()).isEqualTo("Pepe");
                });
    }

    @Test
    void userNotFoundTest() {
        User user1 = User.builder()
                .id(3)
                .build();

        when(repository.findById(user1.getId())).thenReturn(Mono.empty());

        testClient.get()
                .uri("/users/3")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void createUserTest() {
        User user1 = User.builder()
                .id(1)
                .name("Pepe")
                .email("pepe@gmail.com")
                .build();
        Mono<User> monoUser = Mono.just(user1);

        when(repository.save(any())).thenReturn(monoUser);

        testClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user1), User.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(User.class)
                .value(user -> {
                    assertThat(user.getName()).isEqualTo("Pepe");
                });
    }

    @Test
    void failedCreationUserTest() {
        User user = User.builder().build();

        when(repository.save(any())).thenReturn(Mono.empty());

        testClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .isBadRequest();

    }
}