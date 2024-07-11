package timify.com.subject;

import static timify.com.subject.domain.SubjectStatus.ACTIVE;

import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.dto.SubjectRequest.registerSubjectRequest;

public class SubjectConverter {

  public static Subject toSubject(registerSubjectRequest request, Member member, int orderNum) {

    return Subject.builder()
        .title(request.getTitle())
        .status(ACTIVE)
        .member(member)
        .order_num(orderNum)
        .build();
  }

}
