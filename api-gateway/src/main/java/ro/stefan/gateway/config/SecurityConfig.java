package ro.stefan.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
	 @Bean
	    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
	        return http
	                .csrf(ServerHttpSecurity.CsrfSpec::disable)
	                .authorizeExchange(exchange -> exchange
	                        .pathMatchers("/actuator/health", "/actuator/info", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
	                        .permitAll()
	                        .pathMatchers(HttpMethod.GET, "/api/accounts/**", "/api/payments/**", "/api/disputes/**", "/api/fraud/**")
	                        .authenticated()
	                        .pathMatchers("/api/**")
	                        .authenticated()
	                        .anyExchange()
	                        .permitAll()
	                )
	                .httpBasic(Customizer.withDefaults())
	                .build();
	    }
}
