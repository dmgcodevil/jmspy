package com.github.example.repository;

import com.github.aspect.Spyable;
import com.github.example.User;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepository {

    @Spyable
    public User createUser(String first, String second) {
        return new User(first, second);
    }
}
