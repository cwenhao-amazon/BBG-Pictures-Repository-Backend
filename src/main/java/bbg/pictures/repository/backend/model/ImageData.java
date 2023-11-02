package bbg.pictures.repository.backend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageData {

//    @Jacksonized
//    @Builder
//    private ImageData(final String path, final String uploadTimestamp, final String uploaderName, final String album) {
//        this.path = path;
//        this.uploadTimestamp = uploadTimestamp;
//        this.uploaderName = uploaderName;
//        this.album = album;
//    }

    @Id
    @GeneratedValue
    private Long id;

    private String path;
    private String uploadTimestamp;
    private String uploaderName;
    private String album;
}
