package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.Gender;
import timify.com.domain.enums.LoginType;
import timify.com.domain.enums.MemberStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 50)
    private String job;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private LoginType login_type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private MemberStatus status;

    // subject 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Subject> subjectList = new ArrayList<>();

    // studyType 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyType> studyTypeList = new ArrayList<>();

    // studyMethod 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyMethod> studyMethodList = new ArrayList<>();

    // studyPlace 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyPlace> studyPlaceList = new ArrayList<>();

    // todo 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Todo> todoList = new ArrayList<>();

    // studyTime 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyTime> studyTimeList = new ArrayList<>();

    // MemberMission 양방향 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberMission> memberMissionList = new ArrayList<>();
}
