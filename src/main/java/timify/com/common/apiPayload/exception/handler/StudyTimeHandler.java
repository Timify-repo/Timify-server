package timify.com.common.apiPayload.exception.handler;

import timify.com.common.apiPayload.code.BaseErrorCode;
import timify.com.common.apiPayload.exception.GeneralException;

public class StudyTimeHandler extends GeneralException {
    public StudyTimeHandler(BaseErrorCode code) {
        super(code);
    }
}
