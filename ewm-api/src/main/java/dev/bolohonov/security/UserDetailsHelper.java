package dev.bolohonov.security;

import dev.bolohonov.server.model.User;
import dev.bolohonov.server.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsHelper {

    @Autowired
    private RoleService roleService;

    public UserDetailsImpl buildDetails(User user) {
        List<GrantedAuthority> authorities = roleService.getRolesForUser(user).stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(), user.getName(), user.getEmail(), user.getPassword_hash(), authorities);
    }
}
