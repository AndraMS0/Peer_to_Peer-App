package com.nagarro.peertopeerapplication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nagarro.peertopeerapplication.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(Set.of(
            USER_READ,
            USER_UPDATE,
            USER_DELETE,
            USER_CREATE
    )),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    )
    ;
    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
