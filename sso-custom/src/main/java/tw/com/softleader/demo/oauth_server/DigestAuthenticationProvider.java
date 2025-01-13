package tw.com.softleader.demo.oauth_server;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class DigestAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) {
    String token = (String) authentication.getPrincipal();

    // 向外部系統請求用戶資料 FIXME 目前為模擬資料
    UserDetails userDetails = fetchUserFromExternalSystem(token);
    if (userDetails == null) {
      throw new BadCredentialsException("Invalid token");
    }

    return new UsernamePasswordAuthenticationToken(
      userDetails,
      null,
      userDetails.getAuthorities()
    );
  }

  private UserDetails fetchUserFromExternalSystem(String token) {
    // 模擬外部系統
    if ("valid-token".equals(token)) {
      return org.springframework.security.core.userdetails.User
        .withUsername("user")
        .password("") // 密碼不使用
        .authorities("ROLE_USER")
        .build();
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

}
