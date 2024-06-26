package timify.com.common.apiPayload.exception;

import org.springframework.security.core.AuthenticationException;
import timify.com.common.apiPayload.code.status.ErrorStatus;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(ErrorStatus code) {
        super(code.name());
    }

}
