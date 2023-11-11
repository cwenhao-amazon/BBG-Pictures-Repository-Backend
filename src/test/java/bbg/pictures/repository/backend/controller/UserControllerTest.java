package bbg.pictures.repository.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.response.SuccessResponse;
import bbg.pictures.repository.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest(classes = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userServiceMock;

    @Autowired
    private UserController objectUnderTest;

    @Test
    void shouldSaveUser() {
        final UserDetails expected = User.withUsername("username").password("password").roles("USER").build();
        doReturn(expected).when(userServiceMock).save(any());

        final ResponseEntity<UserDetails> response = objectUnderTest.saveUser(null);
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("location").get(0)).endsWith("/username");

        verify(userServiceMock).save(any());
    }

    @Test
    void shouldGetUser() {
        final UserDetails expected = User.withUsername("username").password("password").roles("USER").build();
        doReturn(expected).when(userServiceMock).findByUsername("username");

        final ResponseEntity<UserDetails> response = objectUnderTest.getUser("username");
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(userServiceMock).findByUsername("username");
    }

    @Test
    void shouldUpdateUser() {
        final String expected = "Successfully updated password";

        final ResponseEntity<SuccessResponse> response = objectUnderTest.updateUser(new UpdatePasswordDto());
        assertThat(response.getBody().getMessage()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(userServiceMock).updatePassword(any());
    }

    @Test
    void shouldDeleteUser() {
        final String expected = "Successfully deleted user with username: 'username'";

        final ResponseEntity<SuccessResponse> response = objectUnderTest.deleteUser("username");
        assertThat(response.getBody().getMessage()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(userServiceMock).delete("username");
    }
}