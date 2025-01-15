package tw.com.softleader.demo.oauth_server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<Void> externalLogin(HttpServletRequest request, HttpSession session) {
    var digest = Optional.ofNullable(request.getHeader("Digest"))
      .or(() -> Optional.ofNullable(request.getParameter("digest")))
      .orElseThrow(() -> new IllegalArgumentException("Digest is missing"));
    session.setAttribute("Digest", digest);
    var headers = new HttpHeaders();
    headers.setLocation(UriComponentsBuilder.fromUriString("http://host.docker.internal:4180/").build().toUri());
    return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
  }

}
