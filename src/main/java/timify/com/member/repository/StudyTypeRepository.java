package timify.com.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.domain.StudyType;

public interface StudyTypeRepository extends JpaRepository<StudyType, Long> {
}
