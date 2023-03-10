package org.camunda.custom.operate.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.camunda.custom.operate.exception.TechnicalException;
import org.camunda.custom.operate.jsonmodel.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class SecurityUtils {
  public static final String PREFIX_TOKEN = "Bearer ";
  public static final String SECRET_KEY =
      "SomethingFixedToSimplify"; // UUID.randomUUID().toString();
  private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  private SecurityUtils() {}

  public static String cryptPwd(String clear) throws TechnicalException {
    return bCryptPasswordEncoder.encode(clear);
  }

  public static boolean matches(String pwd, String encodedpassword) {
    return bCryptPasswordEncoder.matches(pwd, encodedpassword);
  }

  public static String getJWTToken(User user) {
    return getJWTToken(user.getUsername(), user.getEmail(), user.getRoles());
  }

  public static String getJWTToken(String username, String email, Set<String> roles) {
    List<String> grantedAuthorities = new ArrayList<String>();
    for (String role : roles) {
      grantedAuthorities.add("ROLE_" + role);
    }

    UserPrincipal principal = new UserPrincipal();
    principal.setUsername(username);
    principal.setEmail(email);

    String token =
        Jwts.builder()
            .setId("CamundaJwt")
            .setSubject(username)
            .claim("principal", principal)
            .claim("authorities", grantedAuthorities)
            .setExpiration(new Date(System.currentTimeMillis() + 5 * 86400000))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
            .compact();

    return token;
  }

  @SuppressWarnings("unchecked")
  public static UserPrincipal getConnectedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    LinkedHashMap<String, Object> principalUser =
        (LinkedHashMap<String, Object>) authentication.getPrincipal();
    String username = (String) principalUser.get("username");
    String email = (String) principalUser.get("email");
    return new UserPrincipal(username, email);
  }

  public static boolean hasRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().toLowerCase().equals("role_" + role.toLowerCase())) {
        return true;
      }
    }
    return false;
  }
}
