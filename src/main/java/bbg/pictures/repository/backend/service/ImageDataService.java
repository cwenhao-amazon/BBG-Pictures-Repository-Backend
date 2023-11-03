package bbg.pictures.repository.backend.service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.repository.ImageDataRepository;
import bbg.pictures.repository.backend.validation.ImageDataValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ImageDataService {
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataValidator validator;

    public void save(final ImageData imageData) {
        validator.validateOnSave(imageData);

        try {
            imageDataRepository.save(imageData);
        } catch (final DataIntegrityViolationException ex) {
            throw new IllegalStateException("Image on path '" + imageData.getId() + "' already exists");
        }

    }

    public Iterable<ImageData> findAllMatchingFilters(final String uploaderName, final String album) {
        final Iterable<ImageData> images = imageDataRepository.findAll();
        return StreamSupport.stream(images.spliterator(), false)
                            .filter(imageData -> matchesFilters(imageData, uploaderName, album))
                            .toList();
    }

    public ImageData findById(final Long id) {
        final Optional<ImageData> imageOptional = imageDataRepository.findById(id);

        if (imageOptional.isPresent()) {
            return imageOptional.get();
        } else {
            throw new EntityNotFoundException("Image by id '" + id + "' does not exist");
        }
    }

    public void update(final Long id, final ImageData imageData) {
        validator.validateOnUpdate(imageData);

        imageDataRepository.findById(id).ifPresentOrElse(
            imageToUpdate -> {
                partialUpdate(imageToUpdate, imageData);
                imageDataRepository.save(imageToUpdate);
            },
            () -> {
                throw new EntityNotFoundException("Image by id '" + id + "' does not exist");
            }
        );
    }

    public void delete(final Long id) {
        imageDataRepository.deleteById(id);
    }

    private boolean matchesFilters(final ImageData imageData, final String uploaderName, final String album) {

        return (Objects.equals(imageData.getUploaderName(), uploaderName) || uploaderName == null)
                && (Objects.equals(imageData.getAlbum(), album) || album == null);
    }
    private void partialUpdate(final ImageData imageToUpdate, final ImageData imageFromUpdate) {
        Optional.ofNullable(imageFromUpdate.getAlbum()).ifPresent(imageToUpdate::setAlbum);
    }
}
