package bbg.pictures.repository.backend.model.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class UserDtoTest {
    @SneakyThrows
    @Test
    void shouldDeserialize() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final UserDto actual = objectMapper.readValue(ResourceUtils.getFile("classpath:user_dto.json"), UserDto.class);

        final UserDto expected = new UserDto();
        expected.setUsername("username");
        expected.setPassword("password");

        assertThat(actual).isEqualTo(expected);
    }
}