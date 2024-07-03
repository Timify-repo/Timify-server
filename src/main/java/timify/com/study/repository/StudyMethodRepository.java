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

    List<StudyMethod> findAllByMemberAndStatus(Member member, CategoryStatus status);

    Optional<StudyMethod> findByIdAndStatus(Long id, CategoryStatus status);

    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);

    @Query("SELECT COUNT(sm) FROM StudyMethod sm WHERE sm.member = :member AND sm.status = :status")
    long countByMemberAndStatus(@Param("member") Member member, @Param("status") CategoryStatus status);

}
