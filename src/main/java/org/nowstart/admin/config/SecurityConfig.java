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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SuccessHandler successHandler;
    private final AdminServerProperties adminServer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(adminServer.path("/assets/**"), adminServer.path("/login")).permitAll()
                .anyRequest().authenticated())
            .formLogin(formLogin -> formLogin.loginPage(adminServer.path("/login")).successHandler(successHandler))
            .logout(logout -> logout.logoutUrl(adminServer.path("/logout")))
            .rememberMe(rememberMe -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(14 * 24 * 60 * 60))
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}
