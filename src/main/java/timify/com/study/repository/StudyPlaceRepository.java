package timify.com.study.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyPlace;

public interface StudyPlaceRepository extends JpaRepository<StudyPlace, Long> {

    /*
    SELECT *
    FROM study_place
    WHERE member_id = ? AND status = ?;
    */
    List<StudyPlace> findAllByMemberAndStatus(Member member, CategoryStatus status);

    /*
    SELECT *
    FROM study_place
    WHERE member_id = ? AND status = ?;
    */
    Optional<StudyPlace> findByIdAndStatus(Long id, CategoryStatus status);

    /*
    SELECT EXISTS (
        SELECT 1
        FROM study_place
        WHERE member_id = ? AND title = ? AND status = ?
    );
    */
    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);

    /*
    SELECT COUNT(*)
    FROM study_place
    WHERE member_id = ? AND status = ?;
    */
    @Query("SELECT COUNT(sp) FROM StudyPlace sp WHERE sp.member = :member AND sp.status = :status")
    long countByMemberAndStatus(@Param("member") Member member,
        @Param("status") CategoryStatus status);

    List<StudyPlace> findAllByMember(Member member);
}
