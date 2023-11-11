package bbg.pictures.repository.backend.validation;

import static bbg.pictures.repository.backend.test_utils.BuilderUtils.updatePasswordDto;
import static bbg.pictures.repository.backend.test_utils.BuilderUtils.user;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserValidator.class)
class UserValidatorTest {
    @Autowired
    private UserValidator objectUnderTest;

    @Test
    void whenUserIsValid_onValidateOnSave_shouldNotThrowAnyExceptions() {
        final UserDto user = user("username", "password");

        assertThatCode(() -> objectUnderTest.validateOnSave(user)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidSave")
    void whenUserIsInvalid_onValidateOnSave_shouldThrowException(final String username,
                                                                 final String password,
                                                                 final Exception ex) {
        final UserDto user = user(username, password);

        assertThatThrownBy( () -> objectUnderTest.validateOnSave(user))
                .isInstanceOf(ex.getClass())
                .hasMessage(ex.getMessage());
    }

    @Test
    void whenNewPasswordIsValid_onValidateOnUpdate_shouldNotThrowAnyExceptions() {
        final UpdatePasswordDto updatePasswordDto = updatePasswordDto("oldPw", "newPw");

        assertThatCode(() -> objectUnderTest.validateOnUpdate(updatePasswordDto)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidUpdate")
    void whenNewPasswordIsInvalid_onValidateOnUpdate_shouldThrowException(final String oldPassword,
                                                                          final String newPassword,
                                                                          final Exception ex) {
        final UpdatePasswordDto updatePasswordDto = updatePasswordDto(oldPassword, newPassword);

        assertThatThrownBy(() -> objectUnderTest.validateOnUpdate(updatePasswordDto))
                .isInstanceOf(ex.getClass())
                .hasMessage(ex.getMessage());
    }

    private static Stream<Arguments> provideForInvalidSave() {
        return Stream.of(
                Arguments.of(null, "password", new IllegalArgumentException("Must set field: 'username'")),
                Arguments.of("username", null, new IllegalArgumentException("Must set field: 'password'")),
                Arguments.of("", "password", new IllegalArgumentException("Must set field: 'username'")),
                Arguments.of("username", "", new IllegalArgumentException("Must set field: 'password'")));
    }

    private static Stream<Arguments> provideForInvalidUpdate() {
        return Stream.of(
                Arguments.of("oldPw", null, new IllegalArgumentException("Password cannot be blank or empty")),
                Arguments.of("oldPw", "", new IllegalArgumentException("Password cannot be blank or empty")));
    }
}