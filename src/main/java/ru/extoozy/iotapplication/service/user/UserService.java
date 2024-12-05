package ru.extoozy.iotapplication.service.user;

import ru.extoozy.iotapplication.store.model.UserEntity;

public interface UserService {
    UserEntity create(UserEntity user);

    UserEntity getUserByUsername(String username);

    UserEntity getById(Long id);
}
