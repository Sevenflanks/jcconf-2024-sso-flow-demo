package tw.com.softleader.demo.oauth_server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.Optional;

/**
 * 處理 Digest 成為登入資訊的 Filter.
 * 目的是取得 Digest 解析並設定為登入資訊且儲存到 session
 */
public class DigestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger log = LoggerFactory.getLogger(DigestAuthenticationFilter.class);

  public DigestAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
    AuthenticationManager authenticationManager) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    var token = Optional.ofNullable(request.getSession(false))
      .map(session -> session.getAttribute("Digest"))
      .map(String.class::cast)
      .or(() -> Optional.ofNullable(request.getHeader("Digest")))
      .or(() -> Optional.ofNullable(request.getParameter("Digest")))
      .orElse(null);

    if (token == null || token.isEmpty()) {
      throw new AuthenticationServiceException("Token is missing");
    }

    // FIXME SimpleGrantedAuthority內容應從token解析而得, 目前事先寫死 for demo
    var authRequest = new PreAuthenticatedAuthenticationToken(token, token, List
      .of(new SimpleGrantedAuthority("twjug")));
    this.setDetails(request, authRequest);
    return this.getAuthenticationManager().authenticate(authRequest);
  }

  protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }
}
