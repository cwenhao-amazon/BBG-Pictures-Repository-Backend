package bbg.pictures.repository.backend.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class ImageDataTest {
    @SneakyThrows
    @Test
    void shouldSerialize() {
        final ImageData imageData = new ImageData();
        imageData.setId(1L);
        imageData.setPath("path");
        imageData.setUploadTimestamp("uploadTimestamp");
        imageData.setUploaderName("uploaderName");
        imageData.setAlbum("album");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(imageData);

        final File jsonFile = ResourceUtils.getFile("classpath:image_data.json");
        final String expected = Files.readString(jsonFile.toPath());

        assertThat(actual).isEqualTo(expected);
    }

    @SneakyThrows
    @Test
    void shouldDeserialize() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final ImageData actual = objectMapper.readValue(ResourceUtils.getFile("classpath:image_data.json"), ImageData.class);

        final ImageData expected = new ImageData();
        expected.setId(1L);
        expected.setPath("path");
        expected.setUploadTimestamp("uploadTimestamp");
        expected.setUploaderName("uploaderName");
        expected.setAlbum("album");

        assertThat(actual).isEqualTo(expected);
    }
}