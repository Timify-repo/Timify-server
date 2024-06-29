package timify.com.study;

import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyResponse;

public class StudyConverter {
    public static StudyType toStudyType(String title, int order) {
        return StudyType.builder()
                .title(title)
                .orderNum(order)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    public static StudyResponse.studyTypeDto toStudyTypeDto(StudyType studyType) {
        return StudyResponse.studyTypeDto.builder()
                .studyTypeId(studyType.getId())
                .order(studyType.getOrderNum())
                .studyTypeTitle(studyType.getTitle())
                .build();
    }

    public static StudyMethod toStudyMethod(String title, int order) {
        return StudyMethod.builder()
                .title(title)
                .orderNum(order)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    public static StudyResponse.studyMethodDto toStudyMethodDto(StudyMethod studyMethod) {
        return StudyResponse.studyMethodDto.builder()
                .studyMethodId(studyMethod.getId())
                .order(studyMethod.getOrderNum())
                .studyMethodTitle(studyMethod.getTitle())
                .build();
    }

    public static StudyPlace toStudyPlace(String title, int order) {
        return StudyPlace.builder()
                .title(title)
                .orderNum(order)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    public static StudyResponse.studyPlaceDto toStudyPlaceDto(StudyPlace studyPlace) {
        return StudyResponse.studyPlaceDto.builder()
                .studyPlaceId(studyPlace.getId())
                .order(studyPlace.getOrderNum())
                .studyPlaceTitle(studyPlace.getTitle())
                .build();
    }
}
