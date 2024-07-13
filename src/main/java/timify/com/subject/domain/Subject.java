package timify.com.subject.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.StudyTime;
import timify.com.domain.Todo;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

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

    public void updateOrderNum(int newOrderNum) {
        this.orderNum = newOrderNum;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void linkFromMember(Member member) {
        this.member = member;
        member.addSubject(this);
    }


    public void unlinkFromMember() {
        if (this.member != null) {
            this.member.getSubjectList().remove(this);
            this.member = null;
        }
    }

}
