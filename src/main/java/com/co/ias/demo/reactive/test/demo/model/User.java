package com.co.ias.demo.reactive.test.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Getter
@ToString
@AllArgsConstructor
@Table(name = "account")
public class User {
    @Id
    private int id;
    private String name;
    private String email;
}
