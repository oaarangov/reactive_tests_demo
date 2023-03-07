package com.co.ias.demo.reactive.test.demo.handlers;

import com.co.ias.demo.reactive.test.demo.model.User;
import com.co.ias.demo.reactive.test.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    @Autowired
    private UserRepository repository;

    public Mono<ServerResponse> listUsers(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(), User.class);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        Mono<User> user = repository.findById(id);
        return user.flatMap(user1 -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(user, User.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> user = request.bodyToMono(User.class);
        return user.flatMap(repository::save)
                .flatMap(user1 -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(user, User.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.BAD_REQUEST).build());
    }
}
