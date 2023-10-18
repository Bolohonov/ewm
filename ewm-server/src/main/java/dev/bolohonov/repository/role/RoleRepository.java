package dev.bolohonov.repository.role;

import dev.bolohonov.model.Role;
import dev.bolohonov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Collection<Role> findAllByUser(User user);
}
