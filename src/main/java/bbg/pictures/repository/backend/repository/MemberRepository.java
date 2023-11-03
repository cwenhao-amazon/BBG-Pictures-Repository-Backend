package bbg.pictures.repository.backend.repository;

import java.util.Optional;

import bbg.pictures.repository.backend.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findMemberByName(final String name);
    @Transactional
    void deleteMemberByName(final String name);
}
