package tw.com.softleader.demo.oauth_server;

import jakarta.servlet.http.Cookie;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DigestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger log = LoggerFactory.getLogger(DigestAuthenticationFilter.class);

  public DigestAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
    AuthenticationManager authenticationManager) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    var token = Optional.ofNullable(request.getHeader("Digest"))
      .or(() -> Optional.ofNullable(request.getParameter("Digest")))
      .or(() -> Optional.ofNullable(request.getSession(false))
        .map(session -> session.getAttribute("Digest"))
        .map(String.class::cast))
      .orElse(null);

    if (token == null || token.isEmpty()) {
      throw new AuthenticationServiceException("Token is missing");
    }

    var authRequest = new PreAuthenticatedAuthenticationToken(token, token, List
      .of(new SimpleGrantedAuthority("twjug")));
    this.setDetails(request, authRequest);
    return this.getAuthenticationManager().authenticate(authRequest);
  }

  protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }
}
