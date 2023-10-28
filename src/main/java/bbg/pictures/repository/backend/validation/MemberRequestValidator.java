package bbg.pictures.repository.backend.validation;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberRequestValidator {
    @Autowired
    private MemberService memberService;
    public void validateOnPatch(final String name, final Member member) {
        if (member.getName() != null) {
            throw new IllegalArgumentException("Updating is forbidden for field: 'name'");
        }

        if (!memberService.existsByName(name)) {
            throw new EntityNotFoundException("Member by name: '" + name + "', does not exists.");
        }
    }

    public void validateOnPost(final Member member) {
        if (memberService.existsByName(member.getName())) {
            throw new IllegalStateException("Member by name: '" + member.getName() + "' already exists.");
        }
    }

    public void validateOnDelete(final String name) {
        if (!memberService.existsByName(name)) {
            throw new EntityNotFoundException("Member by name: '" + name + "', does not exists.");
        }
    }
}
