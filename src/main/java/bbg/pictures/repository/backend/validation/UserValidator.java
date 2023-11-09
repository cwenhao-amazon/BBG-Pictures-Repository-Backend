package bbg.pictures.repository.backend.validation;

import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {
    public void validateOnSave(final UserDto user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Must set field: 'username'");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Must set field: 'password'");
        }
    }

    public void validateOnUpdate(final UpdatePasswordDto passwords) {
        if (passwords.getNewPassword() == null || passwords.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank or empty");
        }
    }
}
