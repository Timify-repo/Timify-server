package timify.com.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StudyResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyTypeDto {

        Long studyTypeId;
        Integer order;
        String studyTypeTitle;
        Boolean isDefault;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyMethodDto {

        Long studyMethodId;
        Integer order;
        String studyMethodTitle;
        Boolean isDefault;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyPlaceDto {

        Long studyPlaceId;
        Integer order;
        String studyPlaceTitle;
        Boolean isDefault;
    }
}
