package ee.ut.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .pathMatchers(HttpMethod.GET, "/events/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/venues/**").hasAnyRole("USER", "ADMIN")

                        .pathMatchers(HttpMethod.POST, "/events/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/events/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/events/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/events/**").hasRole("ADMIN")

                        .pathMatchers(HttpMethod.POST, "/venues/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/venues/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/venues/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/venues/**").hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        var admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}