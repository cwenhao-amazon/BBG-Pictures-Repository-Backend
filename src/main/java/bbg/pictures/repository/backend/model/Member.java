package bbg.pictures.repository.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Entity
@Data
@NoArgsConstructor
public class Member {

    @Jacksonized
    @Builder
    private Member(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String name;
    private String password;
}
