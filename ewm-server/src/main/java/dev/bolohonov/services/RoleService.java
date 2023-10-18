package dev.bolohonov.services;

import dev.bolohonov.model.Role;
import dev.bolohonov.model.User;

import java.util.Collection;

public interface RoleService {

    Collection<Role> getRolesForUser(User user);
}
