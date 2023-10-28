package bbg.pictures.repository.backend.repository;

import bbg.pictures.repository.backend.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    public Member findMemberByName(final String name);
    public boolean existsMemberByName(final String name);
    @Transactional
    public void deleteMemberByName(final String name);

}
