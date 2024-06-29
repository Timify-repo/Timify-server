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
        String studyTypeTitle;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyMethodDto {
        Long studyMethodId;
        String studyMethodTitle;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyPlaceDto {
        Long studyPlaceId;
        String studyPlaceTitle;
    }
}
