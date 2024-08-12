package timify.com.studytime.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.member.domain.Member;
import timify.com.studytime.domain.StudyTime;
import timify.com.todo.domain.Todo;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    @Query("SELECT s FROM StudyTime s " +
        "INNER JOIN s.todo t " +
        "INNER JOIN s.member m " +
        "WHERE t.date = :date " +
        "AND m.id = :memberId")
    List<StudyTime> findAllByTodoDateAndMember(@Param("date") LocalDate date,
        @Param("memberId") Long memberId);

    List<StudyTime> findByMemberAndTodoId(Member member, Long todoId);

    long countByTodo(Todo todo);

    List<StudyTime> findByTodoOrderByStartTimeAsc(Todo todo);

    List<StudyTime> findByTodoAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Todo todo, LocalDateTime endTime, LocalDateTime startTime);
}
