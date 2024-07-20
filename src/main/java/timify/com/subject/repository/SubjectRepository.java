package timify.com.subject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAllByMemberAndStatus(Member member, SubjectStatus status);

    int countByMemberAndStatus(Member member, SubjectStatus status);

    boolean existsByMemberAndTitle(Member member, String title);

}
