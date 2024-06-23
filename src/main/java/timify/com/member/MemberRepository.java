package timify.com.member;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndLoginType(Long socialId, LoginType loginType);

    Optional<Member> findByIdAndSocialId(Long memberId, Long socialId);
}
