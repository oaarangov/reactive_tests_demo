package com.co.ias.demo.reactive.test.demo.components;

import com.co.ias.demo.reactive.test.demo.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;


import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return RouterFunctions.route()
                .path("/users", builder -> builder
                        .GET("", accept(MediaType.APPLICATION_JSON), handler::listUsers)
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), handler::getUser)
                        .POST("", accept(MediaType.APPLICATION_JSON), handler::createUser)
                )
                .build();
    }
}
