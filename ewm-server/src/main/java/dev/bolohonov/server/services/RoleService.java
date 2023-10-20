package dev.bolohonov.server.services;

import dev.bolohonov.server.model.Role;
import dev.bolohonov.server.model.User;

import java.util.Collection;

public interface RoleService {

    Collection<Role> getRolesForUser(User user);
}
