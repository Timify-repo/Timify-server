package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyMethod;

import java.util.List;
import java.util.Optional;

public interface StudyMethodRepository extends JpaRepository<StudyMethod, Long> {

    List<StudyMethod> findAllByMemberAndStatus(Member member, CategoryStatus status);

    Optional<StudyMethod> findByIdAndStatus(Long id, CategoryStatus status);

    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);
}
