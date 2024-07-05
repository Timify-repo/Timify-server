package timify.com.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import timify.com.auth.dto.AuthResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.AuthHandler;

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

    /**
     * 인증 code를 가지고 카카오 API 서버로부터 access token을 받아오는 메소드
     *
     * @param code
     * @return
     */
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

    /**
     * access token을 가지고 카카오 사용자 정보를 불러오는 메소드
     *
     * @param accessToken
     * @return
     */
    public AuthResponse.kakaoResultDto getUserInfo(String accessToken) {

        RestTemplate rt = new RestTemplate();

        String requestUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = rt.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String response = responseEntity.getBody();
                JSONObject jsonResponse = new JSONObject(response);
                long id = jsonResponse.getLong("id");
                String nickname = jsonResponse.getJSONObject("kakao_account").getJSONObject("profile").getString("nickname");
                String email = jsonResponse.getJSONObject("kakao_account").getString("email");

                // log.info("response JSON: {}", response);
                log.info("kakao user id: {}", id);
                log.info("kakao user nickname: {}", nickname);
                log.info("kakao user email: {}", email);

                return AuthResponse.kakaoResultDto.builder()
                        .socialId(id)
                        .nickname(nickname)
                        .email(email).build();

            } else {
                log.error("Failed to get user info. Status code: {}", responseEntity.getStatusCode());
                throw new AuthHandler(ErrorStatus.KAKAO_REQ_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthHandler(ErrorStatus.KAKAO_REQ_FAILED);
        }
    }
}
