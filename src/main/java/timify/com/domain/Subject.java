package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.SubjectStatus;

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

    @Column(nullable = false, length=50)
    private String email;

    @Column(nullable = false, length=3)
    private int order_num;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)",nullable = false)
    private SubjectStatus status;




}
