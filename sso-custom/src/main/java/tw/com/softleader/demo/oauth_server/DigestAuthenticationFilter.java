package tw.com.softleader.demo.oauth_server;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Optional;

import static java.util.Base64.getUrlDecoder;
import static org.springframework.util.StringUtils.hasText;

public class DigestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger log = LoggerFactory.getLogger(DigestAuthenticationFilter.class);

  public DigestAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
    AuthenticationManager authenticationManager) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    String token = Optional.ofNullable(request.getHeader("Digest"))
      .or(() -> Optional.ofNullable(request.getHeader("digest")))
      .orElse(null);

    if (token == null || token.isEmpty()) {
      throw new RuntimeException("Token is missing");
    }

//    // 將 token 傳遞給 AuthenticationProvider
//    return getAuthenticationManager().authenticate(
//      new UsernamePasswordAuthenticationToken(token, null)
//    );
    var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(token, token);
    setDetails(request, authRequest);
    return this.getAuthenticationManager().authenticate(authRequest);
  }

  protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
    Authentication authResult) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);

    //    // 嘗試獲取保存的請求
    //    var savedRequest = requestCache.getRequest(request, response);
    //    if (savedRequest != null) {
    //      // 重定向到原始請求的路徑
    //      response.sendRedirect(savedRequest.getRedirectUrl());
    //    } else {
    //      chain.doFilter(request, response);
    //    }
  }

}
