package timify.com.common.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import timify.com.common.apiPayload.code.BaseErrorCode;
import timify.com.common.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
