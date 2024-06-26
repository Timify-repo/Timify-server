package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.TodoStatus;
import timify.com.member.domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String contents;

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
    @JoinColumn(name = "study_type_id", nullable = false)
    private StudyType studyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_method_id", nullable = false)
    private StudyMethod studyMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_place_id", nullable = false)
    private StudyPlace studyPlace;

    // studyTime 양방향 매핑
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<StudyTime> studyTimeList = new ArrayList<>();
}
