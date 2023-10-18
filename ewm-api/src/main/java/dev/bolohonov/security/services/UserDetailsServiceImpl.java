package dev.bolohonov.security.services;

import dev.bolohonov.model.User;
import dev.bolohonov.repository.user.UserRepository;
import dev.bolohonov.security.UserDetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsHelper detailsHelper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).get();

        return detailsHelper.buildDetails(user);
    }
}
