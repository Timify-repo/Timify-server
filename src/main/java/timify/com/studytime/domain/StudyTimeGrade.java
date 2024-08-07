package timify.com.studytime.domain;

import lombok.Getter;

@Getter
public enum StudyTimeGrade {
    EXCELLENT(2.0),      // 최상
    GOOD(1.5),           // 상
    AVERAGE(1.2),        // 중
    BELOW_AVERAGE(1.0),  // 하
    POOR(1.0);            // 최하

    private double score;

    StudyTimeGrade(double score) {
        this.score = score;
    }

}
