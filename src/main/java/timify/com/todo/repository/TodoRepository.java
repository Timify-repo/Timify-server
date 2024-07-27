package timify.com.todo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.todo.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByMemberAndSubjectId(Member member, Long subjectId);
}
