package bbg.pictures.repository.backend.validation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import bbg.pictures.repository.backend.model.Member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MemberValidator.class)
class MemberValidatorTest {
    @Autowired
    private MemberValidator objectUnderTest;

    @Test
    void whenValidMemberIsPassedToValidateOnSave_shouldNotThrowAnyExceptions() {
        final Member member = member("name", "password");

        assertThatCode(() -> objectUnderTest.validateOnSave(member)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidSave")
    void whenInvalidMemberIsPassedToValidateOnSave_shouldThrowException(final String name,
                                                                        final String password,
                                                                        final Exception exception) {
        final Member member = member(name, password);

        assertThatThrownBy(() -> objectUnderTest.validateOnSave(member))
                .isInstanceOf(exception.getClass())
                .hasMessage(exception.getMessage());
    }

    @Test
    void whenValidMemberIsPassedToValidateOnUpdate_shouldNotThrowAnyExceptions() {
        final Member member = member(null, "password");

        assertThatCode(() -> objectUnderTest.validateOnUpdate(member)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidUpdate")
    void whenInvalidMemberIsPassedToValidateOnUpdate_shouldThrowException(final String name,
                                                                          final String password,
                                                                          final Exception exception) {
        final Member member = member(name, password);

        assertThatThrownBy(() -> objectUnderTest.validateOnUpdate(member))
                .isInstanceOf(exception.getClass())
                .hasMessage(exception.getMessage());
    }

    private static Stream<Arguments> provideForInvalidSave() {
        return Stream.of(
                Arguments.of(null, "password", new IllegalArgumentException("Must set field: 'name'")),
                Arguments.of("name", null, new IllegalArgumentException("Must set field: 'password'")));
    }

    private static Stream<Arguments> provideForInvalidUpdate() {
        return Stream.of(
                Arguments.of("name", "password", new IllegalArgumentException("Updating is forbidden for field: 'name'")));
    }

    private Member member(final String name, final String password) {
        final Member member = new Member();
        member.setName(name);
        member.setPassword(password);

        return member;
    }
}