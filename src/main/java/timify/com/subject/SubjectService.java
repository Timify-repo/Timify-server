package timify.com.subject;

import static timify.com.subject.dto.SubjectResponse.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timify.com.auth.security.SecurityUtil;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.member.MemberService;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.dto.SubjectRequest;
import timify.com.subject.dto.SubjectResponse;
import timify.com.subject.repository.SubjectRepository;

@Service
@RequiredArgsConstructor
public class SubjectService {
  private final SubjectRepository subjectRepository;
  private final MemberService memberService;

  @Transactional
  public subjectInfoDto registerSubject(@Valid SubjectRequest.registerSubjectRequest request) {
    Member findMember = getMember();
    int orderNum = subjectRepository.countByMember(findMember) + 1;
    Subject registerSubject = subjectRepository.save(SubjectConverter.toSubject(request, findMember, orderNum));

    return subjectInfoDto.builder()
        .id(registerSubject.getId())
        .title(registerSubject.getTitle())
        .orderNum(registerSubject.getOrder_num())
        .status(registerSubject.getStatus())
        .createAt(LocalDate.from(registerSubject.getCreatedAt()))
        .build();
  }

  @Transactional
  public getListDto getSubjectAll() {
    Member findMember = getMember();

    List<subjectInfoDto> subjectListAll = subjectRepository.findAllByMember(findMember).stream()
        .map(subject -> subjectInfoDto.builder()
            .id(subject.getId())
            .title(subject.getTitle())
            .orderNum(subject.getOrder_num())
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
    subjectRepository.delete(subject);

    return subjectId;
  }

  @Transactional
  public updateOrderNumDto changeOrder(Long subjectId, int newOrderNum) {
    Subject subject = validateAndGetSubject(subjectId);
    Member member = subject.getMember();
    List<Subject> subjects = subjectRepository.findAllByMember(member);

    // 순서가 전체 갯수를 넘는 경우 에러 메시지 출력
    if (newOrderNum > subjects.size() || newOrderNum < 1) {
      throw new SubjectHandler(ErrorStatus.INVALID_ORDER_NUMBER);
    }

    // 새로운 순서에 해당하는 항목을 찾고 순서 교체
    subjects.forEach(s -> {
      if (s.getOrder_num() == newOrderNum) {
        s.updateOrderNum(subject.getOrder_num());
      }
    });
    subject.updateOrderNum(newOrderNum);

    return updateOrderNumDto.builder()
        .id(subject.getId())
        .orderNum(subject.getOrder_num())
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
    return subject;
  }

  private Member getMember() {
    return memberService.findMember(SecurityUtil.getCurrentMemberId());
  }


}
