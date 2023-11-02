package bbg.pictures.repository.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Member {

//    @Jacksonized
//    @Builder
//    private Member(final String name, final String password) {
//        this.name = name;
//        this.password = password;
//    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String password;
}
