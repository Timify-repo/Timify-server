package timify.com.study.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyType;

public interface StudyTypeRepository extends JpaRepository<StudyType, Long> {

    /*
    SELECT *
    FROM study_type
    WHERE member_id = ? AND status = ?;
    */
    List<StudyType> findAllByMemberAndStatus(Member member, CategoryStatus status);

    /*
    SELECT *
    FROM study_type
    WHERE member_id = ? AND status = ?;
    */
    Optional<StudyType> findByIdAndStatus(Long id, CategoryStatus status);

    /*
    SELECT EXISTS (
        SELECT 1
        FROM study_type
        WHERE member_id = ? AND title = ? AND status = ?
    );
    */
    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);

    /*
    SELECT COUNT(*)
    FROM study_type
    WHERE member_id = ? AND status = ?;
    */
    @Query("SELECT COUNT(st) FROM StudyType st WHERE st.member = :member AND st.status = :status")
    long countByMemberAndStatus(@Param("member") Member member,
        @Param("status") CategoryStatus status);

    List<StudyType> findAllByMember(Member member);
}
