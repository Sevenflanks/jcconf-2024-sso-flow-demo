package tw.jcconf.sso_flow_demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResp {

  String username;
  Set<String> authorities;
  LocalDateTime expireAt;

}
