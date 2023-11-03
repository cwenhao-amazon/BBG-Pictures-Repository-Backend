package bbg.pictures.repository.backend.validation;

import bbg.pictures.repository.backend.model.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberValidator {
    public void validateOnSave(final Member member) {
        if (member.getName() == null) {
            throw new IllegalArgumentException("Must set field: 'name'");
        }

        if (member.getPassword() == null) {
            throw new IllegalArgumentException("Must set field: 'password'");
        }
    }

    public void validateOnUpdate(final Member member) {
        if (member.getName() != null) {
            throw new IllegalArgumentException("Updating is forbidden for field: 'name'");
        }
    }
}
