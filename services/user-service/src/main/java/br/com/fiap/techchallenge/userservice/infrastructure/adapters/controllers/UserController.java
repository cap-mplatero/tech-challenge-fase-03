package br.com.fiap.techchallenge.userservice.infrastructure.adapters.controllers;

import br.com.fiap.techchallenge.userservice.application.dtos.UpdateUserRequest;
import br.com.fiap.techchallenge.userservice.application.dtos.UserResponse;
import br.com.fiap.techchallenge.userservice.application.ports.input.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userUseCase.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userUseCase.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
