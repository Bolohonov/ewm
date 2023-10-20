package dev.bolohonov.server.repository.role;

import dev.bolohonov.server.model.Role;
import dev.bolohonov.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Collection<Role> findAllByUser(User user);
}
