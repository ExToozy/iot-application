package ru.extoozy.iotapplication.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.extoozy.iotapplication.api.dto.LoginResponseDto;
import ru.extoozy.iotapplication.api.dto.RefreshTokenDto;
import ru.extoozy.iotapplication.api.dto.UserDto;
import ru.extoozy.iotapplication.api.mapper.UserMapper;
import ru.extoozy.iotapplication.service.auth.AuthService;
import ru.extoozy.iotapplication.store.model.UserEntity;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper mapper;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Validated UserDto dto) {
        UserEntity userEntity = mapper.toEntity(dto);
        return authService.login(userEntity);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody @Validated UserDto dto) {
        UserEntity user = mapper.toEntity(dto);
        UserEntity registeredUser = authService.register(user);
        return mapper.toDto(registeredUser);
    }

    @PostMapping("/refresh")
    public LoginResponseDto refresh(@RequestBody @Validated RefreshTokenDto dto) {
        return authService.refresh(dto.getRefreshToken());
    }

}
