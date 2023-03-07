package com.co.ias.demo.reactive.test.demo.repository;

import com.co.ias.demo.reactive.test.demo.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByName(String name);

    Mono<User> findByEmail(String email);
}
