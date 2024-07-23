package timify.com.member.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.domain.MemberMission;
import timify.com.domain.StudyTime;
import timify.com.domain.Todo;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.subject.domain.Subject;

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

    private Long socialId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    private Gender gender;

    @Column(length = 50)
    private String job;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private RoleType roleType;

    // subject 양방향 매핑
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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

    // 이름 수정을 위한 메소드
    public void updateName(String name) {
        this.name = name;
    }

    // 생일 수정을 위한 메소드
    public void updateBirth(LocalDate birth) {
        this.birth = birth;
    }

    // 직업 수정을 위한 메소드
    public void updateJob(String job) {
        this.job = job;
    }

    // 성별 수정을 위한 메소드
    public void updateGender(Gender gender) {
        this.gender = gender;
    }
}
