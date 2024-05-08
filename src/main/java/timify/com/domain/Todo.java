package timify.com.domain;

import jakarta.persistence.*;
import lombok.*;
import timify.com.domain.common.BaseDateTimeEntity;
import timify.com.domain.enums.TodoStatus;

import java.time.LocalDate;

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
}
