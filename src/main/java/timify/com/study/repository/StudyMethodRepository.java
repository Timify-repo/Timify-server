package timify.com.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.study.domain.StudyMethod;

public interface StudyMethodRepository extends JpaRepository<StudyMethod, Long> {
}
