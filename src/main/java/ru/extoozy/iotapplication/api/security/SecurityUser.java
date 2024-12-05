package ru.extoozy.iotapplication.api.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.extoozy.iotapplication.store.model.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(UserEntity userEntity) {
        id = userEntity.getId();
        username = userEntity.getUsername();
        password = userEntity.getPassword();
        authorities = new ArrayList<>(List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
