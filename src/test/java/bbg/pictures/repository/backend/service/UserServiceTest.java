package bbg.pictures.repository.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.dto.UserDto;
import bbg.pictures.repository.backend.validation.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@SpringBootTest(classes = UserService.class)
class UserServiceTest {
    @MockBean
    private UserValidator validatorMock;
    @MockBean
    private UserDetailsManager userDetailsServiceMock;
    @MockBean
    private PasswordEncoder passwordEncoderMock;

    @Autowired
    private UserService objectUnderTest;

    @Test
    void shouldNReturnUserDetails_onSave_whenNoConflictingUsernameExists() {
        final UserDetails expected = User.withUsername("username").password("password").roles("USER").build();
        doReturn("password").when(passwordEncoderMock).encode("password");

        final UserDto user = new UserDto();
        user.setUsername("username");
        user.setPassword("password");

        assertThat(objectUnderTest.save(user)).isEqualTo(expected);

        verify(validatorMock).validateOnSave(user);
        verify(userDetailsServiceMock).userExists(user.getUsername());
        verify(passwordEncoderMock).encode(user.getPassword());
        verify(userDetailsServiceMock).createUser(expected);
    }

    @Test
    void shouldThrowException_onSave_whenConflictingUsernameExists() {
        doReturn("password").when(passwordEncoderMock).encode("password");
        doReturn(true).when(userDetailsServiceMock).userExists("username");

        final UserDto user = new UserDto();
        user.setUsername("username");
        user.setPassword("password");

        assertThatThrownBy(() -> objectUnderTest.save(user))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User with username 'username' already exists");

        verify(validatorMock).validateOnSave(user);
        verify(userDetailsServiceMock).userExists(user.getUsername());
        verify(passwordEncoderMock, never()).encode(any());
        verify(userDetailsServiceMock, never()).createUser(any());
    }

    @Test
    void shouldReturnUserDetails_onFindByUsername_whenUserExists() {
        final UserDetails expected = User.withUsername("username").password("password").roles("USER").build();
        doReturn(expected).when(userDetailsServiceMock).loadUserByUsername("username");

        assertThat(objectUnderTest.findByUsername("username")).isEqualTo(expected);

        verify(userDetailsServiceMock).loadUserByUsername("username");
    }

    @Test
    void shouldThrowException_onFindByUsername_whenUserDoesNotExist() {
        doThrow(new UsernameNotFoundException("")).when(userDetailsServiceMock).loadUserByUsername(any());

        assertThatThrownBy(() -> objectUnderTest.findByUsername("username"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with username 'username' does not exist");

        verify(userDetailsServiceMock).loadUserByUsername("username");
    }

    @Test
    void shouldUpdatePassword() {
        doReturn("newPwd").when(passwordEncoderMock).encode("newPwd");

        final UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setOldPassword("oldPwd");
        updatePasswordDto.setNewPassword("newPwd");

        objectUnderTest.updatePassword(updatePasswordDto);

        verify(userDetailsServiceMock).changePassword(updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword());
    }

    @Test
    void shouldNotThrowAnyExceptions_onDelete_whenUserExists() {
        doReturn(true).when(userDetailsServiceMock).userExists("username");

        assertThatCode(() -> objectUnderTest.delete("username")).doesNotThrowAnyException();

        verify(userDetailsServiceMock).userExists("username");
        verify(userDetailsServiceMock).deleteUser("username");
    }

    @Test
    void shouldThrowException_onDelete_whenUserDoesNotExist() {
        doReturn(false).when(userDetailsServiceMock).userExists("username");

        assertThatThrownBy(() -> objectUnderTest.delete("username"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with username 'username' does not exist");

        verify(userDetailsServiceMock).userExists("username");
        verify(userDetailsServiceMock, never()).deleteUser(any());
    }
}