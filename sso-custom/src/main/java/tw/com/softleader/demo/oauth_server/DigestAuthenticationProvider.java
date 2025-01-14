package tw.com.softleader.demo.oauth_server;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

public class DigestAuthenticationProvider extends PreAuthenticatedAuthenticationProvider {

  public DigestAuthenticationProvider() {
    super.setPreAuthenticatedUserDetailsService(token ->
      fetchUserFromExternalSystem((String) token.getCredentials()));
  }

  private UserDetails fetchUserFromExternalSystem(String token) {
    // 模擬外部系統
    // FIXME 應調整為token取user的邏輯
    if ("valid-token".equals(token)) {
      return org.springframework.security.core.userdetails.User
        .withDefaultPasswordEncoder()
        .username("user")
        .password("user")
        .roles("user", "admin")
        .build();
    }
    return null;
  }

}
