package bbg.pictures.repository.backend.service;

import java.util.Optional;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.repository.MemberRepository;
import bbg.pictures.repository.backend.validation.MemberValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberValidator validator;

    public void save(final Member member) {
        validator.validateOnSave(member);

        try {
            memberRepository.save(member);
        } catch (final DataIntegrityViolationException ex) {
            throw new IllegalStateException("Member with name '" + member.getName() + "' already exists");
        }
    }

    public Iterable<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findByName(final String name) {
        final Optional<Member> memberOptional = memberRepository.findMemberByName(name);

        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            throw new EntityNotFoundException("Member with name '" + name + "' does not exist");
        }
    }

    public void update(final String name, final Member member) {
        validator.validateOnUpdate(member);

        memberRepository.findMemberByName(name).ifPresentOrElse(
            memberToUpdate -> {
                partialUpdate(memberToUpdate, member);
                memberRepository.save(memberToUpdate);
            },
            () -> {
                throw new EntityNotFoundException("Member with name '" + name + "' does not exist");
            }
        );
    }

    public void delete(final String name) {
        memberRepository.deleteMemberByName(name);
    }

    private void partialUpdate(final Member memberToUpdate, final Member memberFromUpdate) {
        Optional.ofNullable(memberFromUpdate.getName()).ifPresent(memberToUpdate::setName);
        Optional.ofNullable(memberFromUpdate.getPassword()).ifPresent(memberToUpdate::setPassword);
    }
}
