package timify.com.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 31 * 24 * 60 * 60)
@AllArgsConstructor // 모든 필드를 초기화하는 생성자
@NoArgsConstructor  // 기본 생성자
public class RefreshToken {

    @Id
    private String refreshToken;
    private Long memberId;
    
}
