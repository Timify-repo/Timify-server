package timify.com.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@Slf4j
public class KakaoApiService {


    @Value("${social.kakao.apikey}")
    private String kakaoApiKey;

    @Value("${social.kakao.redirect_uri}")
    private String kakaoRedirectUri;

    // 인증 code를 가지고 카카오 API 서버로부터 access token을 받아오는 메소드
    public String getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();


        String accessToken = "";
        String requestUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client_id", kakaoApiKey);
        parameters.put("redirect_uri", kakaoRedirectUri);
        parameters.put("code", code);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(requestUrl);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = rt.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String response = responseEntity.getBody();
            JSONObject jsonResponse = new JSONObject(response);
            accessToken = jsonResponse.getString("access_token");
            String refreshToken = jsonResponse.getString("refresh_token");

            log.info("response JSON: {}", response);
            log.info("access token: {}", accessToken);
            log.info("refresh token: {}", refreshToken);
        } else {
            log.error("Failed to get access token. Status code: {}", responseEntity.getStatusCode());
        }

        return accessToken;
    }
}
