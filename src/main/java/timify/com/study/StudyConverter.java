package timify.com.study;

import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyResponse;

public class StudyConverter {
    public static StudyType toStudyType(String title, int order) {
        return StudyType.builder()
                .title(title)
                .order_num(order)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    public static StudyResponse.studyTypeDto toStudyTypeDto(StudyType studyType) {
        return StudyResponse.studyTypeDto.builder()
                .studyTypeId(studyType.getId())
                .studyTypeTitle(studyType.getTitle())
                .build();
    }
}
