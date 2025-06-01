package org.nowstart.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthHandler jwtAuthHandler;
    private final AdminServerProperties adminServer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(new AntPathRequestMatcher("/eureka/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(adminServer.path("/assets/**")))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(adminServer.path("/login")))
                .permitAll()
                .anyRequest()
                .authenticated())
            .formLogin(formLogin -> formLogin.successHandler(jwtAuthHandler).loginPage(adminServer.path("/login")))
            .logout(logout -> logout.logoutUrl(adminServer.path("/logout")))
            .rememberMe(rememberMe -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600))
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}
