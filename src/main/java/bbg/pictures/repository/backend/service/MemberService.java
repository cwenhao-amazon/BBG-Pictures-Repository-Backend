package bbg.pictures.repository.backend.service;

import java.util.Optional;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public void save(final Member member) {
        memberRepository.save(member);
    }

    public Iterable<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findByName(final String name) {
        return memberRepository.findMemberByName(name);
    }

    public Member update(final String name, final Member member) {
        final Member memberToUpdate = memberRepository.findMemberByName(name);
        partialUpdate(memberToUpdate, member);
        memberRepository.save(memberToUpdate);

        return memberToUpdate;
    }

    public void delete(final String name) {
        memberRepository.deleteMemberByName(name);
    }

    public boolean existsByName(final String name) {
        return memberRepository.existsMemberByName(name);
    }

    private void partialUpdate(final Member memberToUpdate, final Member memberFromUpdate) {
        Optional.ofNullable(memberFromUpdate.getName()).ifPresent(memberToUpdate::setName);
        Optional.ofNullable(memberFromUpdate.getPassword()).ifPresent(memberToUpdate::setPassword);
    }
}
