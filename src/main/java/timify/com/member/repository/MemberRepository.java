package timify.com.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    /*
    SELECT *
    FROM member
    WHERE social_id = ? AND login_type = ?;
     */
    Optional<Member> findBySocialIdAndLoginType(Long socialId, LoginType loginType);

    /*
    SELECT EXISTS (
        SELECT 1
        FROM member
        WHERE social_id = ? AND login_type = ?
    );
     */
    boolean existsBySocialIdAndLoginType(Long socialId, LoginType loginType);

    /*
    SELECT *
    FROM member
    WHERE member_id = ? AND social_id = ?;
    */
    Optional<Member> findByIdAndSocialId(Long memberId, Long socialId);
}
