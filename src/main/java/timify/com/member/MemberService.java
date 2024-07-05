package timify.com.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.auth.KakaoApiService;
import timify.com.auth.dto.AuthResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoApiService kakaoApiService;

    @Transactional
    public Member kakaoSignin(MemberRequest.kakaoSigninRequest request) {
        AuthResponse.kakaoResultDto userInfo = kakaoApiService.getUserInfo(request.getAccessToken());

        // request의 Gender 값 검증
        String gender = request.getGender();
        if (!"F".equals(gender) && !"M".equals(gender) && !"N".equals(gender)) {
            throw new MemberHandler(ErrorStatus.GENDER_BAD_REQUEST);
        }

        // socialId와 loginType이 일치하는 사용자가 있는지 검증
        boolean isExist = memberRepository.existsBySocialIdAndLoginType(userInfo.getSocialId(), LoginType.KAKAO);
        if (isExist) {
            throw new MemberHandler(ErrorStatus.MEMBER_EXISTS);
        }

        Member member = MemberConverter.toMemberFromKakaoRequest(request, userInfo);
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }


}
