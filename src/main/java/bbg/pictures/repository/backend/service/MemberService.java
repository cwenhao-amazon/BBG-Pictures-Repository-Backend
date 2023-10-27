package bbg.pictures.repository.backend.service;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public void save(Member member) {
        memberRepository.save(member);
    }

    public Iterable<Member> findAll() {
        return memberRepository.findAll();
    }
}
