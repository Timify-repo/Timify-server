package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.study.domain.StudyPlace;

public interface StudyPlaceRepository extends JpaRepository<StudyPlace, Long> {
}
