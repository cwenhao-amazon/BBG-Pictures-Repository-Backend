package bbg.pictures.repository.backend.service;

import static bbg.pictures.repository.backend.test_utils.BuilderUtils.imageData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.repository.ImageDataRepository;
import bbg.pictures.repository.backend.validation.ImageDataValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest(classes = ImageDataService.class)
class ImageDataServiceTest {
    @MockBean
    private ImageDataValidator validatorMock;
    @MockBean
    private ImageDataRepository imageDataRepositoryMock;

    @Autowired
    private ImageDataService objectUnderTest;

    @Test
    void shouldSave_whenNoConflictingPathsExist() {
        final ImageData expected = imageData(1L, "path", "uploadTimestamp", "uploaderName", "album");
        doReturn(expected).when(imageDataRepositoryMock).save(any());

        assertThat(objectUnderTest.save(new ImageData())).isEqualTo(expected);

        verify(validatorMock).validateOnSave(any());
        verify(imageDataRepositoryMock).save(any());
    }

    @Test
    void shouldThrowException_onSave_whenConflictingPathExists() {
        doThrow(DataIntegrityViolationException.class).when(imageDataRepositoryMock).save(any());

        assertThatThrownBy(() -> objectUnderTest.save(new ImageData()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Image on path 'null' already exists");

        verify(validatorMock).validateOnSave(any());
        verify(imageDataRepositoryMock).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideForFindFilters")
    void shouldFindMatchingFilters(final String uploaderName,
                                   final String album,
                                   final List<ImageData> expected) {
        List<ImageData> imageDataList = List.of(
                imageData(1L, "path", "uploadTimestamp", "uploaderNameF", "albumF"),
                imageData(1L, "path", "uploadTimestamp", "uploaderNameF", "album"),
                imageData(1L, "path", "uploadTimestamp", "uploaderName", "albumF"),
                imageData(1L, "path", "uploadTimestamp", "uploaderName", "album"));

        doReturn(imageDataList).when(imageDataRepositoryMock).findAll();

        assertThat(objectUnderTest.findAllMatchingFilters(uploaderName, album)).containsExactlyInAnyOrderElementsOf(expected);

        verify(imageDataRepositoryMock).findAll();
    }

    @Test
    void shouldFindById_whenImageDataExists() {
        final ImageData expected = imageData(1L, "path", "uploaderTimestamp", "uploaderName", "album");
        doReturn(Optional.of(expected)).when(imageDataRepositoryMock).findById(1L);

        assertThat(objectUnderTest.findById(1L)).isEqualTo(expected);

        verify(imageDataRepositoryMock).findById(1L);
    }

    @Test
    void shouldThrowException_onFindById_whenImageDataDoesNotExist() {
        doReturn(Optional.empty()).when(imageDataRepositoryMock).findById(1L);

        assertThatThrownBy(() -> objectUnderTest.findById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Image by id '1' does not exist");

        verify(imageDataRepositoryMock).findById(1L);
    }

    @Test
    void shouldNotThrowAnyExceptions_onUpdate_whenImageDataExists() {
        doReturn(Optional.of(new ImageData())).when(imageDataRepositoryMock).findById(1L);

        assertThatCode(() -> objectUnderTest.update(1L, new ImageData())).doesNotThrowAnyException();

        verify(imageDataRepositoryMock).save(any());
    }

    @Test
    void shouldThrowException_onUpdate_whenImageDataDoesNotExist() {
        doReturn(Optional.empty()).when(imageDataRepositoryMock).findById(1L);

        assertThatThrownBy(() -> objectUnderTest.update(1L, new ImageData()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Image by id '1' does not exist");

        verify(imageDataRepositoryMock, never()).save(any());
    }

    @Test
    void shouldNotThrowAnyExceptions_onDelete_whenImageDataExists() {
        doReturn(true).when(imageDataRepositoryMock).existsById(1L);

        assertThatCode(() -> objectUnderTest.delete(1L)).doesNotThrowAnyException();

        verify(imageDataRepositoryMock).existsById(1L);
        verify(imageDataRepositoryMock).deleteById(1L);
    }

    @Test
    void shouldThrowException_onDelete_whenImageDataDoesNotExist() {
        doReturn(false).when(imageDataRepositoryMock).existsById(1L);

        assertThatThrownBy(() -> objectUnderTest.delete(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Image by id '1' does not exist");

        verify(imageDataRepositoryMock).existsById(1L);
        verify(imageDataRepositoryMock, never()).deleteById(1L);
    }

    private static Stream<Arguments> provideForFindFilters() {
        final ImageData imageDataWithFilteredUploaderNameAndAlbum = imageData(1L, "path", "uploadTimestamp", "uploaderNameF", "albumF");
        final ImageData imageDataWithFilteredUploaderName = imageData(1L, "path", "uploadTimestamp", "uploaderNameF", "album");
        final ImageData imageDataWithFilteredAlbum = imageData(1L, "path", "uploadTimestamp", "uploaderName", "albumF");
        final ImageData imageData = imageData(1L, "path", "uploadTimestamp", "uploaderName", "album");

        return Stream.of(
                Arguments.of("uploaderNameF", "albumF", List.of(imageDataWithFilteredUploaderNameAndAlbum)),
                Arguments.of(null, "albumF", List.of(imageDataWithFilteredUploaderNameAndAlbum, imageDataWithFilteredAlbum)),
                Arguments.of("uploaderNameF", null, List.of(imageDataWithFilteredUploaderNameAndAlbum, imageDataWithFilteredUploaderName)),
                Arguments.of(null, null, List.of(imageDataWithFilteredUploaderNameAndAlbum, imageDataWithFilteredAlbum, imageDataWithFilteredUploaderName, imageData)));
    }
}