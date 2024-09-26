package tw.jcconf.sso_flow_demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hello")
public class HelloController {

  @GetMapping
  public AuthResp hello(Authentication authentication) {
    var jwt = Optional.ofNullable((Jwt) authentication.getCredentials());
    return new AuthResp(
        authentication.getName(),
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toSet()),
        jwt.map(AbstractOAuth2Token::getExpiresAt)
            .map(i -> LocalDateTime.ofInstant(i, ZoneId.systemDefault())).orElse(null)
    );
  }

  @GetMapping("/role/twjug")
  @PreAuthorize("hasRole('twjug')")
  public String helloTwjug() {
    return "role check success: twjug";
  }

  @GetMapping("/role/admin")
  @PreAuthorize("hasRole('admin')")
  public String helloAdmin() {
    return "role check success: admin";
  }

}
