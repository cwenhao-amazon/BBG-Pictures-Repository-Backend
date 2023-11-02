package bbg.pictures.repository.backend.repository;

import bbg.pictures.repository.backend.model.ImageData;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDataRepository extends CrudRepository<ImageData, Long> {
    boolean existsImageDataByPath(final String path);
}
