package chat.api.user.config;

import chat.api.user.jwt.JwtAccessDeniedHandler;
import chat.api.user.jwt.JwtAuthenticationEntryPoint;
import chat.api.user.jwt.JwtSecurityConfig;
import chat.api.user.jwt.TokenProvider;
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
                                .antMatchers("/ws/chat/**").permitAll()
                                .antMatchers("/h2-console/**").permitAll()
                                .antMatchers("/api/v1/users/login").permitAll()
                                .antMatchers("/api/v1/users/signup").permitAll()
                                .antMatchers("/swagger.html").permitAll()
                                .antMatchers("/v3/api-docs/**").permitAll()
                                .antMatchers("/swagger-resources/**").permitAll()
                                .antMatchers("/swagger-ui/**").permitAll()
                                .antMatchers("/configuration/ui").permitAll()
                                .anyRequest().authenticated())
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
