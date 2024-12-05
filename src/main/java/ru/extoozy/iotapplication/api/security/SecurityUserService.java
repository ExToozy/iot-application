package ru.extoozy.iotapplication.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.extoozy.iotapplication.service.user.UserService;
import ru.extoozy.iotapplication.store.model.UserEntity;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.getUserByUsername(username);
        return new SecurityUser(user);
    }
}
