package br.com.fiap.techchallenge.userservice.application.usecases;

import br.com.fiap.techchallenge.userservice.application.dtos.CreateUserRequest;
import br.com.fiap.techchallenge.userservice.application.ports.input.CreateUserUseCase;
import br.com.fiap.techchallenge.userservice.application.ports.output.PasswordEncoderPort;
import br.com.fiap.techchallenge.userservice.application.ports.output.UserRepositoryPort;
import br.com.fiap.techchallenge.userservice.domain.entities.Role;
import br.com.fiap.techchallenge.userservice.domain.entities.User;

import java.util.Set;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUserUseCaseImpl(UserRepositoryPort userRepository,
                                 PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(CreateUserRequest request) {

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.name(),
                request.email(),
                request.username(),
                encodedPassword,
                Set.of(Role.USER)
        );

        userRepository.save(user);
    }
}