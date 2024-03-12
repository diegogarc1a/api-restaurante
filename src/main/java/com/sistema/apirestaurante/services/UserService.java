package com.sistema.apirestaurante.services;

import com.sistema.apirestaurante.entidades.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll() throws Exception;

    User save(User user) throws Exception;

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
