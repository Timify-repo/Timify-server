package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyType;

import java.util.List;
import java.util.Optional;

public interface StudyTypeRepository extends JpaRepository<StudyType, Long> {

    List<StudyType> findAllByMemberAndStatus(Member member, CategoryStatus status);

    Optional<StudyType> findByIdAndStatus(Long id, CategoryStatus status);

    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);

    @Query("SELECT COUNT(st) FROM StudyType st WHERE st.member = :member AND st.status = :status")
    long countByMemberAndStatus(@Param("member") Member member, @Param("status") CategoryStatus status);
}
