package bbg.pictures.repository.backend.service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.repository.ImageDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageDataService {
    @Autowired
    private ImageDataRepository imageDataRepository;

    public void save(final ImageData imageData) {
        imageDataRepository.save(imageData);
    }

    public Iterable<ImageData> findAllMatchingFilters(final String uploaderName, final String album) {
        final Iterable<ImageData> images = imageDataRepository.findAll();
        return StreamSupport.stream(images.spliterator(), false)
                            .filter(imageData -> matchesFilters(imageData, uploaderName, album))
                            .collect(Collectors.toList());
    }

    public ImageData findById(final Long id) {
        return imageDataRepository.findById(id).get();
    }

    public ImageData update(final Long id, final ImageData imageData) {
        final ImageData imageToUpdate = imageDataRepository.findById(id).get();
        partialUpdate(imageToUpdate, imageData);
        imageDataRepository.save(imageToUpdate);

        return imageToUpdate;
    }

    public void delete(final Long id) {
        imageDataRepository.deleteById(id);
    }

    public boolean existsByPath(final String path) {
        return imageDataRepository.existsImageDataByPath(path);
    }

    public boolean existsById(final Long id) {
        return imageDataRepository.existsById(id);
    }

    private boolean matchesFilters(final ImageData imageData, final String uploaderName, final String album) {

        return (Objects.equals(imageData.getUploaderName(), uploaderName) || uploaderName == null)
                && (Objects.equals(imageData.getAlbum(), album) || album == null);
    }
    private void partialUpdate(final ImageData imageToUpdate, final ImageData imageFromUpdate) {
        Optional.ofNullable(imageFromUpdate.getAlbum()).ifPresent(imageToUpdate::setAlbum);
    }
}
