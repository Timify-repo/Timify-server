package timify.com.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.domain.StudyType;
import timify.com.member.domain.Member;

public interface StudyTypeRepository extends JpaRepository<StudyType, Long> {

    boolean existsByMemberAndTitle(Member member, String title);
}
