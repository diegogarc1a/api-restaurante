package com.sistema.apirestaurante.controller;

import com.sistema.apirestaurante.entidades.User;
import com.sistema.apirestaurante.services.JpaUserDetailsService;
import com.sistema.apirestaurante.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    private final JpaUserDetailsService jpaUserDetailsService;

    public UserController(UserService userService, JpaUserDetailsService jpaUserDetailsService) {
        this.userService = userService;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @GetMapping("/")
    public List<User> list() throws Exception{
        return userService.findAll();
    }
    @GetMapping("/{username}")
    public Optional<User> findByUser(@PathVariable("username") String username) throws Exception{
        return userService.findByUsername(username);
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) throws Exception{
        if( result.hasFieldErrors() ){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) throws Exception{
        user.setAdmin(false);
        return create(user, result);
    }

    @GetMapping("/actual-usuario")
    public UserDetails findByUser(Principal principal) throws Exception{
        return jpaUserDetailsService.loadUserByUsername(principal.getName());
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach( err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errors);
    }
}
