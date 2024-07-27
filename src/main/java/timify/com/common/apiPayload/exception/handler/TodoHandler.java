package timify.com.common.apiPayload.exception.handler;

import timify.com.common.apiPayload.code.BaseErrorCode;
import timify.com.common.apiPayload.exception.GeneralException;

public class TodoHandler extends GeneralException {

    public TodoHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
