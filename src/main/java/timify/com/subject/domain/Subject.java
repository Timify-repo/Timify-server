package timify.com.subject.domain;

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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.domain.StudyTime;
import timify.com.domain.Todo;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.member.domain.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Subject extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 3)
    private int orderNum;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    private SubjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // todo 양방향 매핑
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Todo> todoList = new ArrayList<>();

    // studyTime 양방향 매핑
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<StudyTime> studyTimeList = new ArrayList<>();

    // 연관관계 메소드
    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getSubjectList().remove(this);
        }
        this.member = member;
        this.member.getSubjectList().add(this);
    }

    public void updateOrderNum(int newOrderNum) {
        this.orderNum = newOrderNum;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateStatus(SubjectStatus subjectStatus) {
        this.status = subjectStatus;
    }
}
