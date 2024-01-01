package br.com.nestworld.gestmanagerFinancyProductApi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.nestworld.gestmanagerFinancyProductApi.infra.security.filters.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
        .csrf(csrf-> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.POST,"/store/register").permitAll()
        .requestMatchers(HttpMethod.POST,"/store/auth").permitAll()
        .requestMatchers(HttpMethod.POST,"/products").hasRole("USER")
        .requestMatchers(HttpMethod.GET,"/products/allProducts").hasRole("USER")
        .requestMatchers(HttpMethod.GET,"/products/allCategory").hasRole("USER")
        .requestMatchers(HttpMethod.POST,"/products/getByName").hasRole("USER")
        .requestMatchers(HttpMethod.POST,"/products/getByCategory").hasRole("USER")
        .requestMatchers(HttpMethod.PUT, "/products/update").hasRole("USER")
         .requestMatchers(HttpMethod.DELETE,"/products/**").hasRole("USER")
          .requestMatchers(HttpMethod.DELETE,"/products/{id}").hasRole("USER")
        .anyRequest().authenticated()
        )
        .addFilterBefore(securityFilter,UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
