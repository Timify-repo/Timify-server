package timify.com.todo.domain;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.member.domain.Member;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.studytime.domain.StudyTime;
import timify.com.subject.domain.Subject;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Todo extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private TodoStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_type_id")
    private StudyType studyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_method_id")
    private StudyMethod studyMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_place_id")
    private StudyPlace studyPlace;

    // studyTime 양방향 매핑
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<StudyTime> studyTimeList = new ArrayList<>();

    // 연관관계 메소드
    public void associateMember(Member member) {
        if (this.member != null) {
            this.member.getTodoList().remove(this);
        }
        this.member = member;
        this.member.getTodoList().add(this);
    }

    // 연관관계 해제 메소드
    public void disassociateMember(Member member) {
        if (member != null) {
            member.getTodoList().remove(this);
            this.member = null;
        }
    }

    // 연관관계 메소드
    public void associateSubject(Subject subject) {
        if (this.subject != null) {
            this.subject.getTodoList().remove(this);
        }
        this.subject = subject;
        this.subject.getTodoList().add(this);
    }

    // 연관관계 해제 메소드
    public void disassociateSubject(Subject subject) {
        if (subject != null) {
            subject.getTodoList().remove(this);
            this.subject = null;
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void updateStudyType(StudyType studyType) {
        this.studyType = studyType;
    }

    public void updateStudyMethod(StudyMethod studyMethod) {
        this.studyMethod = studyMethod;
    }

    public void updateStudyPlace(StudyPlace studyPlace) {
        this.studyPlace = studyPlace;
    }
}
