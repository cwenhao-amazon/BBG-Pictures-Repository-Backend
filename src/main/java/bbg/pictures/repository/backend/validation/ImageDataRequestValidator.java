package bbg.pictures.repository.backend.validation;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.service.ImageDataService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageDataRequestValidator {
    @Autowired
    private ImageDataService imageDataService;

    public void validateOnPost(final ImageData imageData) {
        if (imageData.getId() != null) {
            throw new IllegalArgumentException("Cannot set field: 'id'");
        }

        if (imageData.getPath() == null) {
            throw new IllegalArgumentException("Must set field: 'path'");
        }

        if (imageData.getUploadTimestamp() == null) {
            throw new IllegalArgumentException("Must set field: 'upload_timestamp'");
        }

        if (imageData.getUploaderName() == null) {
            throw new IllegalArgumentException("Must set field: 'uploader_name'");
        }

        if (imageData.getAlbum() == null) {
            throw new IllegalArgumentException("Must set field: 'album'");
        }

        if (imageDataService.existsByPath(imageData.getPath())) {
            throw new IllegalStateException("Image by path: '" + imageData.getPath() + "' already exists.");
        }
    }

    public void validateOnGet(final Long id) {
        if (!imageDataService.existsById(id)) {
            throw new EntityNotFoundException("Image by id: '" + id + "' does not exists.");
        }
    }

    public void validateOnPatch(final Long id, final ImageData imageData) {
        if (imageData.getId() != null) {
            throw new IllegalArgumentException("Updating is forbidden for field: 'id'");
        }

        if (imageData.getPath() != null) {
            throw new IllegalArgumentException("Updating is forbidden for field: 'path'");
        }

        if (imageData.getUploadTimestamp() != null) {
            throw new IllegalArgumentException("Updating is forbidden for field: 'upload_timestamp'");
        }

        if (imageData.getUploaderName() != null) {
            throw new IllegalArgumentException("Updating is forbidden for field: 'uploader_name'");
        }

        if (!imageDataService.existsById(id)) {
            throw new EntityNotFoundException("Image by id: '" + id + "' does not exists.");
        }
    }

    public void validateOnDelete(final Long id) {
        if (!imageDataService.existsById(id)) {
            throw new EntityNotFoundException("Image by id: '" + id + "' does not exists.");
        }
    }
}
