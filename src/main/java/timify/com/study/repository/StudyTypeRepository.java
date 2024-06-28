package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyType;

import java.util.List;

public interface StudyTypeRepository extends JpaRepository<StudyType, Long> {

    List<StudyType> findAllByMemberAndStatus(Member member, CategoryStatus status);
}
