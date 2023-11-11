package bbg.pictures.repository.backend.controller;

import static bbg.pictures.repository.backend.test_utils.BuilderUtils.imageData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.model.response.SuccessResponse;
import bbg.pictures.repository.backend.service.ImageDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = ImageDataController.class)
class ImageDataControllerTest {
    @MockBean
    private ImageDataService imageDataServiceMock;

    @Autowired
    private ImageDataController objectUnderTest;

    @Test
    void shouldSaveImage() {
        final ImageData expected = imageData(1L, "path", "uploadTimestamp", "uploaderName", "album");
        doReturn(expected).when(imageDataServiceMock).save(any());

        final ResponseEntity<ImageData> response = objectUnderTest.saveImage(new ImageData());
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("location").get(0)).endsWith("/1");

        verify(imageDataServiceMock).save(any());
    }

    @Test
    void shouldGetImages() {
        final Iterable<ImageData> expected = List.of(
                imageData(1L, "path", "uploadTimestamp", "uploaderName", "album"),
                imageData(1L, "path", "uploadTimestamp", "uploaderName", "album"));
        doReturn(expected).when(imageDataServiceMock).findAllMatchingFilters(any(), any());

        final ResponseEntity<Iterable<ImageData>> response = objectUnderTest.getImages(null, null);
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(imageDataServiceMock).findAllMatchingFilters(null, null);
    }

    @Test
    void shouldGetImage() {
        final ImageData expected = imageData(1L, "path", "uploadTimestamp", "uploaderName", "album");
        doReturn(expected).when(imageDataServiceMock).findById(1L);

        final ResponseEntity<ImageData> response = objectUnderTest.getImage(1L);
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(imageDataServiceMock).findById(1L);
    }

    @Test
    void shouldUpdateImage() {
        final String expected = "Successfully updated image with id: '1'";

        final ResponseEntity<SuccessResponse> response = objectUnderTest.updateImage(1L, null);
        assertThat(response.getBody().getMessage()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(imageDataServiceMock).update(eq(1L), any());
    }

    @Test
    void shouldDeleteImage() {
        final String expected = "Successfully deleted image with id: '1'";

        final ResponseEntity<SuccessResponse> response = objectUnderTest.deleteImage(1L);
        assertThat(response.getBody().getMessage()).isEqualTo(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(imageDataServiceMock).delete(1L);
    }
}