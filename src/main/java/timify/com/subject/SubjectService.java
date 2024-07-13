package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.auth.security.SecurityUtil;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.member.MemberService;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;
import timify.com.subject.dto.SubjectRequest;
import timify.com.subject.repository.SubjectRepository;

@Service
@RequiredArgsConstructor
public class SubjectService {
  private final SubjectRepository subjectRepository;
  private final MemberService memberService;

  @Transactional
  public subjectInfoDto registerSubject(@Valid subjectRequest request) {
    Member findMember = getMember();

    long activeSubjectCount = subjectRepository.countByMemberAndStatus(findMember, SubjectStatus.ACTIVE);

    // 최대 개수 확인
    if (activeSubjectCount >= 15) {
      throw new SubjectHandler(ErrorStatus.MAX_SUBJECT_ERROR);
    }

    int orderNum =  subjectRepository.countByMemberAndStatus(findMember, SubjectStatus.ACTIVE) + 1;
    Subject registerSubject = subjectRepository.save(SubjectConverter.toSubject(request, findMember, orderNum));
    findMember.addSubject(registerSubject);

    return subjectInfoDto.builder()
        .id(registerSubject.getId())
        .title(registerSubject.getTitle())
        .orderNum(registerSubject.getOrderNum())
        .status(registerSubject.getStatus())
        .createAt(LocalDate.from(registerSubject.getCreatedAt()))
        .build();
  }

  @Transactional(readOnly = true)
  public getListDto getSubjectAll() {
    Member findMember = getMember();
    List<subjectInfoDto> subjectListAll = subjectRepository.findAllByMemberAndStatus(findMember, SubjectStatus.ACTIVE).stream()
        .map(subject -> subjectInfoDto.builder()
            .id(subject.getId())
            .title(subject.getTitle())
            .orderNum(subject.getOrderNum())
            .status(subject.getStatus())
            .createAt(LocalDate.from(subject.getCreatedAt()))
            .build())
        .collect(Collectors.toList());

    return getListDto.builder()
        .subjects(subjectListAll)
        .build();
  }

  @Transactional
  public Long deleteSubject(Long subjectId) {
    Subject subject = validateAndGetSubject(subjectId);
    Member member = subject.getMember();
    member.removeSubject(subject);
    subjectRepository.delete(subject);

    return subjectId;
  }

  @Transactional
  public updateOrderNumDto changeOrder(Long subjectId, int newOrderNum) {
    Subject subject = validateAndGetSubject(subjectId);
    Member member = subject.getMember();
    List<Subject> subjects = subjectRepository.findAllByMemberAndStatus(member, SubjectStatus.ACTIVE);

    // 순서가 전체 갯수를 넘는 경우 에러 메시지 출력
    if (newOrderNum > subjects.size() || newOrderNum < 1) {
      throw new SubjectHandler(ErrorStatus.INVALID_ORDER_NUMBER);
    }

    // 새로운 순서에 해당하는 항목을 찾고 순서 교체
    subjects.forEach(s -> {
      if (s.getOrderNum() == newOrderNum) {
        s.updateOrderNum(subject.getOrderNum());
      }
    });
    subject.updateOrderNum(newOrderNum);

    return updateOrderNumDto.builder()
        .id(subject.getId())
        .orderNum(subject.getOrderNum())
        .updateAt(LocalDate.from(subject.getCreatedAt()))
        .build();
  }

  @Transactional
  public updateTitleNameDto updateTitle(@Valid subjectRequest request, Long id) {
    Subject subject = validateAndGetSubject(id);
    subject.updateTitle(request.getTitle());

    return updateTitleNameDto.builder()
        .id(subject.getId())
        .title(subject.getTitle())
        .updateAt(LocalDate.from(subject.getCreatedAt()))
        .build();
  }

  private Subject validateAndGetSubject(Long subjectId) {
    Member findMember = getMember();
    Subject subject = subjectRepository.findById(subjectId)
        .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

    if (!subject.getMember().equals(findMember)) {
      throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
    }

    if (subject.getStatus() != SubjectStatus.ACTIVE) {
      throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
    }

    return subject;
  }

  private Member getMember() {
    return memberService.findMember(SecurityUtil.getCurrentMemberId());
  }


}
