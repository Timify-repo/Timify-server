package timify.com.subject.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAllByMember(Member member);

    List<Subject> findAllByMemberAndStatus(Member member, SubjectStatus subjectStatus);

    int countByMemberAndStatus(Member member, SubjectStatus status);

    boolean existsByMemberAndTitle(Member member, String title);

    @Query("SELECT DISTINCT s FROM Subject s " +
        "JOIN s.todoList t " +
        "WHERE s.member.id = :memberId " +
        "AND t.date = :date " +
        "AND s.status = 'INACTIVE' " +
        "AND t.subject.id = s.id")
    List<Subject> findInactiveSubjectsWithTodosOnDate(@Param("date") LocalDate date,
        @Param("memberId") Long memberId);

    List<Subject> findAllByMemberAndStatusOrderByOrderNumAsc(Member member, SubjectStatus status);
}
