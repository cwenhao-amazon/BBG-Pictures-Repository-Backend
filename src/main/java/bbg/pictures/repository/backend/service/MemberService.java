package bbg.pictures.repository.backend.service;

import java.util.Optional;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.repository.MemberRepository;
import bbg.pictures.repository.backend.validation.MemberValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberValidator validator;

    public Member save(final Member member) {
        validator.validateOnSave(member);

        if (memberRepository.existsById(member.getName())) {
            log.error("Member entity conflict occurred");
            throw new IllegalStateException("Member with name '" + member.getName() + "' already exists");
        }

        return memberRepository.save(member);
    }

    public Iterable<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findByName(final String name) {
        final Optional<Member> memberOptional = memberRepository.findById(name);

        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            log.error("Could not find member entity: " + name);
            throw new EntityNotFoundException("Member with name '" + name + "' does not exist");
        }
    }

    public void update(final String name, final Member member) {
        validator.validateOnUpdate(member);

        memberRepository.findById(name).ifPresentOrElse(
            memberToUpdate -> {
                partialUpdate(memberToUpdate, member);
                memberRepository.save(memberToUpdate);
            },
            () -> {
                log.error("Could not find member entity: " + name);
                throw new EntityNotFoundException("Member with name '" + name + "' does not exist");
            }
        );
    }

    public void delete(final String name) {
        memberRepository.deleteById(name);
    }

    private void partialUpdate(final Member memberToUpdate, final Member memberFromUpdate) {
        Optional.ofNullable(memberFromUpdate.getName()).ifPresent(memberToUpdate::setName);
        Optional.ofNullable(memberFromUpdate.getPassword()).ifPresent(memberToUpdate::setPassword);
    }
}
