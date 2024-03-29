package chat.api.config;

import chat.api.jwt.JwtAccessDeniedHandler;
import chat.api.jwt.JwtAuthenticationEntryPoint;
import chat.api.jwt.JwtSecurityConfig;
import chat.api.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests(request ->
                        request
                                .antMatchers(
                                        "/node_modules/**",
                                        "/ws/chat/**",
                                        "/h2-console/**",
                                        "/auth/**",
                                        "/swagger.html",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/swagger-ui/**",
                                        "/configuration/ui"
                                ).permitAll()
                                .anyRequest().authenticated())
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
