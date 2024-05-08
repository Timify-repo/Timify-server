package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.CategoryStatus;
import timify.com.domain.enums.SubjectStatus;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyType extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_type_id")
    private Long id;

    @Column(nullable = false, length=30)
    private String title;

    @Column(nullable = false, length=3)
    private int order_num;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)",nullable = false)
    private CategoryStatus status;

}
