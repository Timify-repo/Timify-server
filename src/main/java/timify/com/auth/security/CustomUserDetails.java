package timify.com.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.RoleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    Long memberId;
    Long socialId;
    LoginType loginType;
    RoleType roleType;

    public CustomUserDetails(Long memberId, Long socialId, LoginType loginType, RoleType roleType) {
        this.memberId = memberId;
        this.socialId = socialId;
        this.loginType = loginType;
        this.roleType = roleType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    // override를 위한 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + roleType);


        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return memberId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
