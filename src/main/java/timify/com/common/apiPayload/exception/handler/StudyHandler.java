package timify.com.common.apiPayload.exception.handler;

import timify.com.common.apiPayload.code.BaseErrorCode;
import timify.com.common.apiPayload.exception.GeneralException;

public class StudyHandler extends GeneralException {
    public StudyHandler(BaseErrorCode code) {
        super(code);
    }
}
