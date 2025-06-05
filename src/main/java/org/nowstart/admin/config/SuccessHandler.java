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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${encrypt.key}")
    private String key;
    private final AdminServerProperties adminServer;
    private static final long EXPIRATION_TIME_MILLIS = 1000 * 60 * 60L; // 1시간
    private static final int SECRET_KEY_MIN_LENGTH = 32;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String jwt = Jwts.builder()
            .subject(authentication.getName())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
            .signWith(Keys.hmacShaKeyFor(StringUtils.leftPad(key, SECRET_KEY_MIN_LENGTH, '0').getBytes()))
            .compact();

        Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (EXPIRATION_TIME_MILLIS / 1000));

        response.addCookie(cookie);
        response.sendRedirect(adminServer.getContextPath());
    }
}
