package tw.com.softleader.demo.oauth_server;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class DigestAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(DigestAuthenticationFilter.class);

  public DigestAuthenticationFilter(String path) {
    this.path = path;
  }

  private final String path;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (path.equals(request.getServletPath())) {
      // 從查詢字串中提取 token
      String token = request.getHeader("digest");
      if (token == null || token.isEmpty()) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing");
        return;
      }
      // 自外部系統驗證 token
      if (!isTokenValid(token)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        return;
      }
      // 若驗證成功 FIXME 目前固定塞user
      var userToken = new UsernamePasswordAuthenticationToken("user", token, List.of(new SimpleGrantedAuthority("ROLE_USER")));
      SecurityContextHolder.getContext().setAuthentication(userToken);
    }
    // 繼續處理請求
    filterChain.doFilter(request, response);
  }

  private boolean isTokenValid(String token) {
    // FIXME 此處為模擬外部呼叫
    return "valid-token".equals(token);
  }

}
