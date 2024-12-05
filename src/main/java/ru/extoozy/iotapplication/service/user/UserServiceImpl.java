package ru.extoozy.iotapplication.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.extoozy.iotapplication.api.exception.AlreadyExistsException;
import ru.extoozy.iotapplication.api.exception.NotFoundException;
import ru.extoozy.iotapplication.store.model.UserEntity;
import ru.extoozy.iotapplication.store.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException(
                    "User with '%s' username already exists".formatted(user.getUsername())
            );
        }

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(
                        "User with '%s' username not found".formatted(username)
                ));
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with id '%s' not found".formatted(id)
                ));

    }
}
