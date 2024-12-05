package ru.extoozy.iotapplication.service.auth;

import ru.extoozy.iotapplication.api.dto.LoginResponseDto;
import ru.extoozy.iotapplication.store.model.UserEntity;

public interface AuthService {
    LoginResponseDto login(UserEntity user);

    UserEntity register(UserEntity user);

    LoginResponseDto refresh(String refreshToken);
}
