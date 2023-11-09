package bbg.pictures.repository.backend.service;

import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.dto.UserDto;
import bbg.pictures.repository.backend.validation.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserService {
    @Autowired
    private UserValidator validator;
    @Autowired
    private UserDetailsManager userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails save(final UserDto user) {
        validator.validateOnSave(user);

        if (userDetailsService.userExists(user.getUsername())) {
            throw new IllegalStateException("User with username '" + user.getUsername() + "' already exists");
        }

        final UserDetails userDetails = User.withUsername(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles("USER")
                .build();

        userDetailsService.createUser(userDetails);

        return userDetails;
    }

    public UserDetails findByUsername(final String username) {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (final UsernameNotFoundException ex) {
            throw new EntityNotFoundException("User with username '" + username + "' does not exist");
        }
    }

    public void updatePassword(final UpdatePasswordDto passwords) {
        validator.validateOnUpdate(passwords);

        userDetailsService.changePassword(passwords.getOldPassword(), passwordEncoder.encode(passwords.getNewPassword()));
    }

    public void delete(final String username) {
        if (userDetailsService.userExists(username)) {
            throw new EntityNotFoundException("User with username '" + username + "' does not exist");
        }

        userDetailsService.deleteUser(username);
    }
}
