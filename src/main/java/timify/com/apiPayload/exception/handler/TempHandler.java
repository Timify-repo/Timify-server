package timify.com.apiPayload.exception.handler;

import timify.com.apiPayload.code.BaseErrorCode;
import timify.com.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
