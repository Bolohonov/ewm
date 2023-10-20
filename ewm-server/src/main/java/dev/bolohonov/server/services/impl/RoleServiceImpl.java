package dev.bolohonov.server.services.impl;

import dev.bolohonov.server.model.Role;
import dev.bolohonov.server.model.User;
import dev.bolohonov.server.repository.role.RoleRepository;
import dev.bolohonov.server.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Collection<Role> getRolesForUser(User user) {
        return roleRepository.findAllByUser(user);
    }
}
