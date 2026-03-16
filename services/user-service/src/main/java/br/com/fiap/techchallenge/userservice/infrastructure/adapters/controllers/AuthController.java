package br.com.fiap.techchallenge.userservice.infrastructure.adapters.controllers;

import br.com.fiap.techchallenge.userservice.application.dtos.CreateUserRequest;
import br.com.fiap.techchallenge.userservice.application.dtos.LoginRequest;
import br.com.fiap.techchallenge.userservice.application.ports.input.AuthenticateUserUseCase;
import br.com.fiap.techchallenge.userservice.application.ports.input.CreateUserUseCase;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(CreateUserUseCase createUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase){

        this.createUserUseCase=createUserUseCase;
        this.authenticateUserUseCase=authenticateUserUseCase;

    }

    @PostMapping("/register")
    public void register(@RequestBody CreateUserRequest request){

        createUserUseCase.execute(request);

    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){

        return authenticateUserUseCase.execute(request);

    }

}