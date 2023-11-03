package bbg.pictures.repository.backend.validation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import bbg.pictures.repository.backend.model.ImageData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ImageDataValidator.class)
class ImageDataValidatorTest {
    @Autowired
    private ImageDataValidator objectUnderTest;

    @Test
    void whenValidImageDataIsPassedToValidateOnSave_shouldNotThrowAnyExceptions() {
        final ImageData imageData = imageData(null, "path", "uploadTimestamp", "uploaderName", "album");

        assertThatCode(() -> objectUnderTest.validateOnSave(imageData)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidSave")
    void whenInvalidImageDataIsPassedToValidateOnSave_shouldThrowException(final Long id,
                                                                           final String path,
                                                                           final String uploadTimestamp,
                                                                           final String uploaderName,
                                                                           final String album,
                                                                           final Exception exception) {
        ImageData imageData = imageData(id, path, uploadTimestamp, uploaderName, album);

        assertThatThrownBy(() -> objectUnderTest.validateOnSave(imageData))
                .isInstanceOf(exception.getClass())
                .hasMessage(exception.getMessage());
    }

    @Test
    void whenValidImageDataIsPassedToValidateOnUpdate_shouldNotThrowAnyExceptions() {
        final ImageData imageData = imageData(null, null, null, null, "album");

        assertThatCode(() -> objectUnderTest.validateOnUpdate(imageData)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidUpdate")
    void whenInvalidImageDataIsPassedToValidateOnUpdate_shouldThrowException(final Long id,
                                                                             final String path,
                                                                             final String uploadTimestamp,
                                                                             final String uploaderName,
                                                                             final String album,
                                                                             final Exception exception) {
        ImageData imageData = imageData(id, path, uploadTimestamp, uploaderName, album);

        assertThatThrownBy(() -> objectUnderTest.validateOnUpdate(imageData))
                .isInstanceOf(exception.getClass())
                .hasMessage(exception.getMessage());
    }

    private static Stream<Arguments> provideForInvalidSave() {
        return Stream.of(
                Arguments.of(1L, "path", "uploadTimestamp", "uploaderName", "album", new IllegalArgumentException("Cannot set field: 'id'")),
                Arguments.of(null, null, "uploadTimestamp", "uploaderName", "album", new IllegalArgumentException("Must set field: 'path'")),
                Arguments.of(null, "path", null, "uploaderName", "album", new IllegalArgumentException("Must set field: 'upload_timestamp'")),
                Arguments.of(null, "path", "uploadTimestamp", null, "album", new IllegalArgumentException("Must set field: 'uploader_name'")),
                Arguments.of(null, "path", "uploadTimestamp", "uploaderName", null, new IllegalArgumentException("Must set field: 'album'")));
    }

    private static Stream<Arguments> provideForInvalidUpdate() {
        return Stream.of(
                Arguments.of(1L, null, null, null, "album", new IllegalArgumentException("Updating is forbidden for field: 'id'")),
                Arguments.of(null, "path", null, null, "album", new IllegalArgumentException("Updating is forbidden for field: 'path'")),
                Arguments.of(null, null, "uploadTimestamp", null, "album", new IllegalArgumentException("Updating is forbidden for field: 'upload_timestamp'")),
                Arguments.of(null, null, null, "uploaderName", "album", new IllegalArgumentException("Updating is forbidden for field: 'uploader_name'")));
    }

    private ImageData imageData(final Long id,
                                final String path,
                                final String uploadTimestamp,
                                final String uploaderName,
                                final String album) {
        final ImageData imageData = new ImageData();
        imageData.setId(id);
        imageData.setPath(path);
        imageData.setUploadTimestamp(uploadTimestamp);
        imageData.setUploaderName(uploaderName);
        imageData.setAlbum(album);

        return imageData;
    }
}