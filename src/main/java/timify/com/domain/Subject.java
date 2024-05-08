package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.SubjectStatus;

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
    private String email;

    @Column(nullable = false, length = 3)
    private int order_num;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    private SubjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // todo 양방향 매핑
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Todo> todoList = new ArrayList<>();

    // studyTime 양방향 매핑
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<StudyTime> studyTimeList = new ArrayList<>();
}
