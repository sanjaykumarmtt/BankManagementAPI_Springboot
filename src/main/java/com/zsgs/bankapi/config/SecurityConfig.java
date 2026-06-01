package com.zsgs.bankapi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zsgs.bankapi.service.managerauthservice.AuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Autowired
    // ;

    @Bean
    @Lazy
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JWtFilter jWtFilter) throws Exception {

        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();

                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/accounts/review/?*").permitAll()
                        .requestMatchers("/api/accounts/review/manager/?*").hasAnyAuthority("MANAGER", "USER")
                        .requestMatchers("/api/accounts/manager/search/?*").hasAuthority("MANAGER")
                        .requestMatchers("/api/accounts/user/**").hasAuthority("USER")
                        .requestMatchers("/api/accounts/**").hasAuthority("MANAGER")

                        .anyRequest().authenticated());
        // http.formLogin(form->form.permitAll().defaultSuccessUrl("/api/auth/defalt"));
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jWtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private Object requestMatchers(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    @Bean
    public UserDetailsService userDetailService() {

        // UserDetails user=User.withUsername("sanjay")
        // .password(paseeEncoder.encode("sanjay123"))
        // .roles("USER")
        // .build();
        // System.out.print(user);
        // UserDetails admin=User.withUsername("kumar")
        // .password(paseeEncoder.encode("sanjay123"))
        // .roles("ADMIN")
        // .build();
        return new AuthService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authDAO = new DaoAuthenticationProvider();
        authDAO.setUserDetailsService(userDetailService());
        authDAO.setPasswordEncoder(passwordEncoder());

        return authDAO;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authenticationProvider()));
    }

}
