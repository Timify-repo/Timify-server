package timify.com.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timify.com.apiPayload.code.status.ErrorStatus;
import timify.com.apiPayload.exception.handler.TempHandler;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TempController {

    @GetMapping("/test/error")
    public String apiResponseTest() {
        throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }

    @GetMapping("/health")
    public String test(){
        return "a";
    }
}
