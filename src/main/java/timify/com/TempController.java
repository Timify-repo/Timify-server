package timify.com;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.TempHandler;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TempController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Hello";
    }

    @GetMapping("/test/error")
    public String apiResponseTest() {
        throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }


}
