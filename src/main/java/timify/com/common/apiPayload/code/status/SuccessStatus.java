package timify.com.common.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import timify.com.common.apiPayload.code.BaseCode;
import timify.com.common.apiPayload.code.ReasonDTO;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    JOIN_SUCCESS(HttpStatus.OK, "MEMBER200", "회원가입 성공"),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "AUTH200", "토큰 재발급 성공"),
    SUBJECT_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "DELETE200", "항목 삭제 성공"),
    ORDER_CHANGE_SUCCESS(HttpStatus.OK, "ORDER200", "항목 순서 변경 성공"),
    TITLE_CHANGE_SUCCESS(HttpStatus.OK, "SUBJECT200", "항목 이름 변경 성공"),
    STORE_SUBJECT_SUCCESS(HttpStatus.OK, "STORE200", "보관함으로 이동 성공"),
    RESTORE_SUBJECT_SUCCESS(HttpStatus.OK, "STORE200", "홈으로 이동 성공");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
