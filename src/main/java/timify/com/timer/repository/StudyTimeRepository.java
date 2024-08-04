package timify.com.timer.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.timer.domain.StudyTime;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    @Query("SELECT s FROM StudyTime s " +
        "INNER JOIN s.todo t " +
        "INNER JOIN s.member m " +
        "WHERE t.date = :date " +
        "AND m.id = :memberId")
    List<StudyTime> findAllByTodoDateAndMember(@Param("date") LocalDate date,
        @Param("memberId") Long memberId);

}
