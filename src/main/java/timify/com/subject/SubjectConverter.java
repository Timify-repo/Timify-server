package timify.com.subject;

import static timify.com.subject.domain.SubjectStatus.ACTIVE;

import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.dto.SubjectRequest.subjectRequest;

public class SubjectConverter {

    public static Subject toSubject(subjectRequest request, int orderNum) {

        return Subject.builder()
            .title(request.getTitle())
            .status(ACTIVE)
            .orderNum(orderNum)
            .build();
    }
}
