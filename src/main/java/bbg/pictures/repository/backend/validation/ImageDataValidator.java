package bbg.pictures.repository.backend.validation;

import bbg.pictures.repository.backend.model.ImageData;
import org.springframework.stereotype.Service;

@Service
public class ImageDataValidator {
    public void validateOnSave(final ImageData imageData) {
        if (imageData.getId() != null) {
            throw new IllegalArgumentException("Cannot set field: 'id'");
        }

        if (imageData.getPath() == null || imageData.getPath().isBlank()) {
            throw new IllegalArgumentException("Must set field: 'path'");
        }

        if (imageData.getUploadTimestamp() == null || imageData.getUploadTimestamp().isBlank()) {
            throw new IllegalArgumentException("Must set field: 'upload_timestamp'");
        }

        if (imageData.getUploaderName() == null || imageData.getUploaderName().isBlank()) {
            throw new IllegalArgumentException("Must set field: 'uploader_name'");
        }

        if (imageData.getAlbum() == null || imageData.getAlbum().isBlank()) {
            throw new IllegalArgumentException("Must set field: 'album'");
        }
    }

    public void validateOnUpdate(final ImageData imageData) {
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
    }
}
