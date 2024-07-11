package timify.com.common.apiPayload.exception.handler;

import timify.com.common.apiPayload.code.BaseErrorCode;
import timify.com.common.apiPayload.exception.GeneralException;

public class SubjectHandler extends GeneralException {
  public SubjectHandler(BaseErrorCode code) {
    super(code);
  }
}
