package timify.com.study.domain;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.member.domain.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyPlace extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_place_id")
    private Long id;

    @Column(nullable = false, length = 30)
    @Setter
    private String title;

    @Column(nullable = false, length = 3)
    private int orderNum;

    @Column(nullable = false)
    private boolean isDefault;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    @Setter
    private CategoryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 연관관계 메소드
    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getStudyPlaceList().remove(this);
        }
        this.member = member;
        this.member.getStudyPlaceList().add(this);
    }

}
