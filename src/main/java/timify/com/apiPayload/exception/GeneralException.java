package timify.com.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import timify.com.apiPayload.code.BaseErrorCode;
import timify.com.apiPayload.code.ErrorReasonDTO;

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
