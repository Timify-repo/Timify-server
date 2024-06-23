package timify.com.common.apiPayload.exception.handler;

import timify.com.common.apiPayload.code.BaseErrorCode;
import timify.com.common.apiPayload.exception.GeneralException;

public class AuthHandler extends GeneralException {
    public AuthHandler(BaseErrorCode code) {
        super(code);
    }
}
