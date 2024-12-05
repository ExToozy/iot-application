package ru.extoozy.iotapplication.service.auth;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.extoozy.iotapplication.api.dto.LoginResponseDto;
import ru.extoozy.iotapplication.api.security.jwt.JwtTokenProvider;
import ru.extoozy.iotapplication.service.user.UserService;
import ru.extoozy.iotapplication.store.model.UserEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;


    @Test
    void login() {
        UserEntity testUser = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("test")
                .build();

        when(userService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(tokenProvider.createAccessToken(testUser.getId(), testUser.getUsername())).thenReturn("mock_access_token");
        when(tokenProvider.createRefreshToken(testUser.getId(), testUser.getUsername())).thenReturn("mock_refresh_token");

        LoginResponseDto responseDto = authService.login(testUser);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(responseDto).isNotNull();
        softly.assertThat(responseDto.getAccess()).isNotEqualTo("mock_access_token");
        softly.assertThat(responseDto.getRefresh()).isNotEqualTo("mock_refresh_token");

        verify(authenticationManager).authenticate(Mockito.any());
        verify(userService).getUserByUsername(testUser.getUsername());
        verify(tokenProvider).createAccessToken(testUser.getId(), testUser.getUsername());
        verify(tokenProvider).createRefreshToken(testUser.getId(), testUser.getUsername());
    }

    @Test
    void register() {
        UserEntity testUser = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("password")
                .build();

        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encoded_password");
        when(userService.create(testUser)).thenReturn(testUser);

        UserEntity createdUser = authService.register(testUser);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(createdUser).isNotNull();
        softly.assertThat(createdUser.getUsername()).isNotEqualTo("test");
        softly.assertThat(createdUser.getPassword()).isNotEqualTo("encoded_password");

        verify(userService).create(testUser);
    }

    @Test
    void refresh() {
        String testToken = "mock_refresh_token";
        UserEntity testUser = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("password")
                .build();

        when(tokenProvider.isExpiredToken(testToken)).thenReturn(false);
        when(tokenProvider.getClaim(testToken, "id")).thenReturn("1");
        when(userService.getById(1L)).thenReturn(testUser);
        when(tokenProvider.createAccessToken(testUser.getId(), testUser.getUsername())).thenReturn("refreshed_token");

        LoginResponseDto refreshDto = authService.refresh(testToken);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(refreshDto).isNotNull();
        softly.assertThat(refreshDto.getAccess()).isNotEqualTo("refreshed_token");
        softly.assertThat(refreshDto.getRefresh()).isNotEqualTo("mock_refresh_token");

        verify(tokenProvider).isExpiredToken(testToken);
        verify(tokenProvider).getClaim(testToken, "id");
        verify(tokenProvider).createAccessToken(testUser.getId(), testUser.getUsername());
        verify(userService).getById(1L);
    }
}