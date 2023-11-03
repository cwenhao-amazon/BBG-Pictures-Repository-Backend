package bbg.pictures.repository.backend.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class MemberTest {
    @SneakyThrows
    @Test
    void shouldSerialize() {
        final Member member = new Member();
        member.setName("name");
        member.setPassword("password");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(member);

        final File jsonFile = ResourceUtils.getFile("classpath:member.json");
        final String expected = Files.readString(jsonFile.toPath());

        assertThat(actual).isEqualTo(expected);
    }

    @SneakyThrows
    @Test
    void shouldDeserialize() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Member actual = objectMapper.readValue(ResourceUtils.getFile("classpath:member.json"), Member.class);

        final Member expected = new Member();
        expected.setName("name");
        expected.setPassword("password");

        assertThat(actual).isEqualTo(expected);
    }
}