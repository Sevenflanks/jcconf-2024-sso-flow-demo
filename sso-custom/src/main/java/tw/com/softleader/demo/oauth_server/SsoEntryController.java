package tw.com.softleader.demo.oauth_server;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * 模擬外部SSO登入點, 外部SSO登入完成後需要帶著Header來訪問內部的SSO Auth Server(本APP)
 */
@Controller
public class SsoEntryController {

  @PostMapping("/external-login")
  public ResponseEntity<Void> externalLogin(HttpServletRequest request) {
    var digest = Optional.ofNullable(request.getHeader("Digest"))
      .or(() -> Optional.ofNullable(request.getParameter("digest")))
      .orElseThrow(() -> new IllegalArgumentException("Digest is missing"));
    var headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, ResponseCookie.from("Digest", digest)
      .path("/").httpOnly(true).maxAge(30).build().toString());
    var url = UriComponentsBuilder.fromUriString("http://host.docker.internal:4180/").build().toUri();
    headers.setLocation(url);
    return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
  }

}
