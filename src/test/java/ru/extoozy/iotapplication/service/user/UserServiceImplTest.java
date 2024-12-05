package ru.extoozy.iotapplication.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.extoozy.iotapplication.api.exception.AlreadyExistsException;
import ru.extoozy.iotapplication.api.exception.NotFoundException;
import ru.extoozy.iotapplication.store.model.UserEntity;
import ru.extoozy.iotapplication.store.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_shouldCreateUser_whenUserNotExists() {
        UserEntity mock = UserEntity
                .builder()
                .username("username")
                .build();

        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.save(mock)).thenReturn(mock);

        userService.create(mock);

        verify(userRepository).existsByUsername("username");
        verify(userRepository).save(mock);
    }

    @Test
    void create_shouldThrowException_whenUserExists() {
        UserEntity mock = UserEntity
                .builder()
                .username("username")
                .build();

        when(userRepository.existsByUsername("username")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(mock))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("User with 'username' username already exists");

        verify(userRepository).existsByUsername("username");
    }

    @Test
    void getUserByUsername_shouldGetUser_whenUserExists() {
        UserEntity userMock = new UserEntity();

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(userMock));

        userService.getUserByUsername("userMock");

        verify(userRepository).findByUsername("userMock");
    }

    @Test
    void getUserByUsername_shouldThrowException_whenUserNotExists() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUsername("user"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with 'user' username not found");

        verify(userRepository).findByUsername("user");
    }

    @Test
    void getById_shouldGetUser_whenUserExists() {
        UserEntity userMock = new UserEntity();

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userMock));

        userService.getById(1L);

        verify(userRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenUserNotExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with id '1' not found");

        verify(userRepository).findById(1L);
    }
}