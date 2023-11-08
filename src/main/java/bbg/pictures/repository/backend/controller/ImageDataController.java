package bbg.pictures.repository.backend.controller;

import java.net.URI;
import java.time.LocalDateTime;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.model.response.SuccessResponse;
import bbg.pictures.repository.backend.service.ImageDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/image")
@Slf4j
public class ImageDataController {

    @Autowired
    private ImageDataService imageDataService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<ImageData> saveImage(@RequestBody final ImageData imageData) {
        log.info("POST request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        final ImageData createdImage = imageDataService.save(imageData);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                        .path("/{id}")
                                                        .buildAndExpand(createdImage.getId())
                                                        .toUri();
        return ResponseEntity.created(location).body(createdImage);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<ImageData>> getImages(@RequestParam(required = false) final String uploaderName, @RequestParam(required = false) final String album) {
        log.info("GET request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        final Iterable<ImageData> images = imageDataService.findAllMatchingFilters(uploaderName, album);

        return ResponseEntity.ok().body(images);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<ImageData> getImage(@PathVariable final Long id) {
        log.info("GET request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        final ImageData image = imageDataService.findById(id);

        return ResponseEntity.ok().body(image);
    }

    @PatchMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<SuccessResponse> updateImage(@PathVariable final Long id, @RequestBody final ImageData imageData) {
        log.info("PATCH request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        imageDataService.update(id, imageData);

        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Successfully updated image with id: '" + id + "'")
                                                            .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<SuccessResponse> deleteImage(@PathVariable final Long id) {
        log.info("DELETE request received at {}", ServletUriComponentsBuilder.fromCurrentRequest().build());
        imageDataService.delete(id);

        //TODO Make it so an exception is thrown when the entity with the id does not exists. Currently it returns the same response as it does when it exists.
        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Successfully deleted image with id: '" + id + "'")
                                                            .build();
        return ResponseEntity.ok().body(responseBody);
    }
}
