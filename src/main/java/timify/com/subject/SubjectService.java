package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;
import jakarta.validation.Valid;
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
import timify.com.subject.repository.SubjectRepository;

@Service
@RequiredArgsConstructor
public class SubjectService {
  private final SubjectRepository subjectRepository;
  private final MemberService memberService;

  @Transactional
  public subjectInfoDto registerSubject(@Valid subjectRequest request) {
    Member findMember = getMember();

    if (subjectRepository.countByMemberAndStatus(findMember, SubjectStatus.ACTIVE) >= 15) {
      throw new SubjectHandler(ErrorStatus.MAX_SUBJECT_ERROR);
    }

    if (subjectRepository.existsByMemberAndTitle(findMember, request.getTitle())) {
      throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
    }

    int orderNum =  subjectRepository.countByMemberAndStatus(findMember, SubjectStatus.ACTIVE) + 1;
    Subject registerSubject = subjectRepository.save(SubjectConverter.toSubject(request, findMember, orderNum));
    findMember.addSubject(registerSubject);

    return subjectInfoDto.builder()
        .id(registerSubject.getId())
        .title(registerSubject.getTitle())
        .orderNum(registerSubject.getOrderNum())
        .status(registerSubject.getStatus())
        .build();
  }

  @Transactional(readOnly = true)
  public getListDto activeSubjectAll() {
    Member findMember = getMember();
    List<subjectInfoDto> subjectListAll = subjectRepository.findAllByMemberAndStatus(findMember, SubjectStatus.ACTIVE).stream()
        .map(subject -> subjectInfoDto.builder()
            .id(subject.getId())
            .title(subject.getTitle())
            .orderNum(subject.getOrderNum())
            .status(subject.getStatus())
            .build())
        .collect(Collectors.toList());

    return getListDto.builder()
        .subjects(subjectListAll)
        .build();
  }

  @Transactional
  public Long deleteSubject(Long subjectId) {
    Subject subject = validateAndGetSubject(subjectId, true);
    subject.getMember().removeSubject(subject);
    subjectRepository.delete(subject);

    return subjectId;
  }

  @Transactional
  public updateOrderNumDto changeOrder(Long subjectId, int newOrderNum) {
    Subject subject = validateAndGetSubject(subjectId, true);
    Member member = subject.getMember();
    List<Subject> subjects = subjectRepository.findAllByMemberAndStatus(member, SubjectStatus.ACTIVE);

    if (newOrderNum > subjects.size() || newOrderNum < 1) {
      throw new SubjectHandler(ErrorStatus.INVALID_ORDER_NUMBER);
    }

    subjects.stream()
        .filter(s -> s.getOrderNum() == newOrderNum)
        .forEach(s -> s.updateOrderNum(subject.getOrderNum()));

    subject.updateOrderNum(newOrderNum);

    return updateOrderNumDto.builder()
        .id(subject.getId())
        .orderNum(subject.getOrderNum())
        .build();
  }

  @Transactional
  public updateTitleNameDto updateTitle(@Valid subjectRequest request, Long id) {
    Subject subject = validateAndGetSubject(id, true);

    if (subjectRepository.existsByMemberAndTitle(getMember(), request.getTitle())) {
      throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
    }

    subject.updateTitle(request.getTitle());

    return updateTitleNameDto.builder()
        .id(subject.getId())
        .title(subject.getTitle())
        .build();
  }

  @Transactional
  public void storeSubject(Long subjectId) {
    Subject subject = validateAndGetSubject(subjectId, false);

    if(subject.getStatus().equals(SubjectStatus.INACTIVE)) {
      throw new SubjectHandler(ErrorStatus.NOT_CHANGE_STATUS);
    }

    subject.updateStatus(SubjectStatus.INACTIVE);
    reorderSubject(subject);
  }

  @Transactional
  public void restoreSubject(Long subjectId) {
    Subject subject = validateAndGetSubject(subjectId, false);

    if(subject.getStatus().equals(SubjectStatus.ACTIVE)) {
      throw new SubjectHandler(ErrorStatus.NOT_CHANGE_STATUS);
    }

    subject.updateStatus(SubjectStatus.ACTIVE);
    reorderSubject(subject);

  }

  @Transactional(readOnly = true)
  public getListDto inactiveSubjectAll() {
    Member findMember = getMember();
    List<subjectInfoDto> subjectListAll = subjectRepository.findAllByMemberAndStatus(findMember, SubjectStatus.INACTIVE).stream()
        .map(subject -> subjectInfoDto.builder()
            .id(subject.getId())
            .title(subject.getTitle())
            .orderNum(subject.getOrderNum())
            .status(subject.getStatus())
            .build())
        .collect(Collectors.toList());

    return getListDto.builder()
        .subjects(subjectListAll)
        .build();
  }

  private Subject validateAndGetSubject(Long subjectId, boolean checkActiveStatus) {
    Member findMember = getMember();
    Subject subject = subjectRepository.findById(subjectId)
        .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

    if (!subject.getMember().equals(findMember)) {
      throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
    }

    if (checkActiveStatus && subject.getStatus() != SubjectStatus.ACTIVE) {
      throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
    }

    return subject;
  }

  private void reorderSubject(Subject subject) {
    Member member = subject.getMember();
    List<Subject> activeSubjects = subjectRepository.findAllByMemberAndStatus(member,
        SubjectStatus.ACTIVE);
    for (int i = 0; i < activeSubjects.size(); i++) {
      activeSubjects.get(i).updateOrderNum(i + 1);
    }

    List<Subject> inActiveSubjects = subjectRepository.findAllByMemberAndStatus(member,
        SubjectStatus.INACTIVE);
    for (int i = 0; i < inActiveSubjects.size(); i++) {
      inActiveSubjects.get(i).updateOrderNum(i + 1);
    }
  }

  private Member getMember() {
    return memberService.findMember(SecurityUtil.getCurrentMemberId());
  }


}
