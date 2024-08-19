package timify.com.todo.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.todo.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByMemberAndSubjectIdAndDate(Member member, Long subjectId, LocalDate date);

    List<Todo> findByMemberAndSubjectId(Member member, Long subjectId);

    List<Todo> findAllByMember(Member member);
}
