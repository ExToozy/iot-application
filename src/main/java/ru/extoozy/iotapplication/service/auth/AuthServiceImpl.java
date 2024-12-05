package ru.extoozy.iotapplication.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.extoozy.iotapplication.api.dto.LoginResponseDto;
import ru.extoozy.iotapplication.api.exception.AccessDeniedException;
import ru.extoozy.iotapplication.api.security.jwt.JwtTokenProvider;
import ru.extoozy.iotapplication.service.user.UserService;
import ru.extoozy.iotapplication.store.model.UserEntity;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(UserEntity user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword()
                )
        );


        UserEntity userFromDb = userService.getUserByUsername(user.getUsername());

        return LoginResponseDto.builder()
                .access(tokenProvider.createAccessToken(userFromDb.getId(), userFromDb.getUsername()))
                .refresh(tokenProvider.createRefreshToken(userFromDb.getId(), userFromDb.getUsername()))
                .build();
    }

    @Override
    public UserEntity register(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.create(user);
    }

    @Override
    public LoginResponseDto refresh(String refreshToken) {
        if (tokenProvider.isExpiredToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(tokenProvider.getClaim(refreshToken, "id"));
        UserEntity user = userService.getById(userId);
        return LoginResponseDto.builder()
                .access(tokenProvider.createAccessToken(userId, user.getUsername()))
                .refresh(refreshToken)
                .build();
    }
}
