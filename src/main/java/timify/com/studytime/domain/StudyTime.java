package timify.com.studytime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.todo.domain.Todo;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyTime extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_time_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyTimeGrade grade; // 최상

    @Column(nullable = false)
    private double temp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    public void updateStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void updateEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateGrade(StudyTimeGrade grade) {
        this.grade = grade;
    }

    public void updateTemp(double temp) {
        this.temp = temp;
    }

    public void associateMember(Member member) {
        if (this.member != null) {
            this.member.getStudyTimeList().remove(this);
        }
        this.member = member;
        this.member.getStudyTimeList().add(this);
    }

    public void disassociateMember(Member member) {
        if (member != null) {
            member.getStudyTimeList().remove(this);
            this.member = null;
        }
    }

    public void associateSubject(Subject subject) {
        if (this.subject != null) {
            this.subject.getStudyTimeList().remove(this);
        }
        this.subject = subject;
        this.subject.getStudyTimeList().add(this);
    }

    // 연관관계 해제 메소드
    public void disassociateSubject(Subject subject) {
        if (subject != null) {
            subject.getStudyTimeList().remove(this);
            this.subject = null;
        }
    }

    public void associateTodo(Todo todo) {
        if (this.todo != null) {
            this.todo.getStudyTimeList().remove(this);
        }
        this.todo = todo;
        System.out.println(this.todo.getStudyTimeList());
        this.todo.getStudyTimeList().add(this);
    }

    // 연관관계 해제 메소드
    public void disassociateTodo(Todo todo) {
        if (todo != null) {
            todo.getStudyTimeList().remove(this);
            this.todo = null;
        }
    }


}
