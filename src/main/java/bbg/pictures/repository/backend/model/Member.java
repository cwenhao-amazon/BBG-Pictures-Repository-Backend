package bbg.pictures.repository.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String name;
    private String password;
}
