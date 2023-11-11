package bbg.pictures.repository.backend.model.dto;

import static bbg.pictures.repository.backend.test_utils.BuilderUtils.updatePasswordDto;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class UpdatePasswordDtoTest {
    @SneakyThrows
    @Test
    void shouldDeserialize() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final UpdatePasswordDto actual = objectMapper.readValue(ResourceUtils.getFile("classpath:update_password_dto.json"), UpdatePasswordDto.class);

        final UpdatePasswordDto expected = updatePasswordDto("oldPassword", "newPassword");

        assertThat(actual).isEqualTo(expected);
    }
}