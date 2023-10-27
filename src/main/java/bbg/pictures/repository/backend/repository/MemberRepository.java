package bbg.pictures.repository.backend.repository;

import bbg.pictures.repository.backend.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

}
