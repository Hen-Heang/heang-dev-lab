package com.heang.springmybatistest.thymeleaf.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class User {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private int age;
    private boolean active;
    private String nickname;
    private String bio;
    private String role;
    private String firstName;
    private String lastName;
    private Department department;
}
