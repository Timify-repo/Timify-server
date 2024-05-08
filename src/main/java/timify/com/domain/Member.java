package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.Gender;
import timify.com.domain.enums.LoginType;
import timify.com.domain.enums.MemberStatus;

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

    @Column(nullable = false, length=100)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)",nullable = false)
    private Gender gender;

    @Column(nullable = false,length = 50)
    private String job;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)",nullable = false)
    private LoginType login_type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)",nullable = false)
    private MemberStatus status;


}
