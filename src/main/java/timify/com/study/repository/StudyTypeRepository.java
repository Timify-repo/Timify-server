package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyType;

import java.util.List;
import java.util.Optional;

public interface StudyTypeRepository extends JpaRepository<StudyType, Long> {

    List<StudyType> findAllByMemberAndStatus(Member member, CategoryStatus status);

    Optional<StudyType> findByIdAndStatus(Long id, CategoryStatus status);

    boolean existsByMemberAndTitleAndStatus(Member member, String title, CategoryStatus status);
}
