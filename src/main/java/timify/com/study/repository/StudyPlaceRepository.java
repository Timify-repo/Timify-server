package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyPlace;

import java.util.List;

public interface StudyPlaceRepository extends JpaRepository<StudyPlace, Long> {

    List<StudyPlace> findAllByMemberAndStatus(Member member, CategoryStatus status);
}
