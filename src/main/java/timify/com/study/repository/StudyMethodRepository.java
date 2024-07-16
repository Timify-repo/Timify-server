package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyMethod;

import java.util.List;
import java.util.Optional;

public interface StudyMethodRepository extends JpaRepository<StudyMethod, Long> {

    /*
    SELECT *
    FROM study_method
    WHERE member_id = ? AND status = ?;
     */
    List<StudyMethod> findAllByMemberAndStatus(Member member, CategoryStatus status);

    /*
    SELECT *
    FROM study_method
    WHERE member_id = ? AND status = ?;
    */
    Optional<StudyMethod> findByIdAndStatus(Long id, CategoryStatus status);

    /*
    SELECT EXISTS (
        SELECT 1
        FROM study_method
        WHERE member_id = ? AND title = ? AND status = ?
    );
     */
    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);

    /*
    SELECT COUNT(*)
    FROM study_method
    WHERE member_id = ? AND status = ?;
     */
    @Query("SELECT COUNT(sm) FROM StudyMethod sm WHERE sm.member = :member AND sm.status = :status")
    long countByMemberAndStatus(@Param("member") Member member, @Param("status") CategoryStatus status);

}
