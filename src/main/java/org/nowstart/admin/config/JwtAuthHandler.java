package org.nowstart.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthHandler implements AuthenticationSuccessHandler {

    @Value("${spring.security.user.password}")
    private String secretKey;
    private final AdminServerProperties adminServer;
    private static final long EXPIRATION_TIME = 60 * 60L;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String jwt = Jwts.builder()
            .subject(authentication.getName())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(Keys.hmacShaKeyFor(StringUtils.leftPad(secretKey, 32, '0').getBytes()))
            .compact();

        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (EXPIRATION_TIME / 1000));

        response.addCookie(cookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        response.sendRedirect(adminServer.getContextPath());
    }
}
