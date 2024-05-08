package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberMission extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_mission_id")
    private Long id;
}
