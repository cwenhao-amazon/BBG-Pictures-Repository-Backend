package bbg.pictures.repository.backend.model.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

class ErrorResponseTest {
    @SneakyThrows
    @Test
    void shouldSerialize() {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .timestamp(LocalDateTime.of(2023, 1, 1, 1, 0, 0)) // 2023-01-01 01:00:00
                                                         .status(HttpStatus.BAD_REQUEST.value())
                                                         .error("error")
                                                         .build();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);

        final File jsonFile = ResourceUtils.getFile("classpath:error_response.json");
        final String expected = Files.readString(jsonFile.toPath());

        assertThat(actual).isEqualTo(expected);
    }
}