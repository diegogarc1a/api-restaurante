package com.sistema.apirestaurante.validation;

import com.sistema.apirestaurante.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    private final UserService userService;

    public ExistsByUsernameValidation(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if( userService == null){
            return true;
        }
        return !userService.existsByUsername(username);
    }
}
