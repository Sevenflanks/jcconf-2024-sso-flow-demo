package tw.com.softleader.demo.oauth_server;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * 用於將 Digest 的資訊轉換為真正的 User 資料
 */
public class DigestAuthenticationProvider extends PreAuthenticatedAuthenticationProvider {

  public DigestAuthenticationProvider() {
    super.setPreAuthenticatedUserDetailsService(this::fetchUserFromExternalSystem);
  }

  private UserDetails fetchUserFromExternalSystem(PreAuthenticatedAuthenticationToken token) {
    // 模擬外部系統
    // FIXME 應調整為token取user的邏輯, 目前是寫死 for demo
    if ("valid-token".equals(token.getCredentials())) {
      return org.springframework.security.core.userdetails.User
        .withDefaultPasswordEncoder()
        .username("user")
        .password("user")
        .authorities(token.getAuthorities())
        .build();
    }
    return null;
  }

}
