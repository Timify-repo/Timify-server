package timify.com.subject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

  int countByMember(Member findMember);

  List<Subject> findAllByMember(Member findMember);
}
