package uz.sar7ar.springcore.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import uz.sar7ar.springcore.model.entities.Roles;
import uz.sar7ar.springcore.security.brute_force_protector.BruteForceProtectionFilter;
import uz.sar7ar.springcore.security.brute_force_protector.LoginAttemptService;
import uz.sar7ar.springcore.security.jwt.JwtRequestFilter;
import uz.sar7ar.springcore.security.jwt.JwtUtil;
import uz.sar7ar.springcore.security.user_details.CustomAuthenticationFilter;
import uz.sar7ar.springcore.security.user_details.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final BruteForceProtectionFilter bruteForceProtectionFilter;
    private final LoginAttemptService loginAttemptService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .userDetailsService(userDetailsService)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/h2-console/*").permitAll()
                        .requestMatchers("/api-docs/**","/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        .requestMatchers("/api/v1/user/login").permitAll()
                        .requestMatchers("/api/v1/user/logout").permitAll()
                        .requestMatchers("/api/v1/user/trainee-registration", "/api/v1/user/trainer-registration").permitAll()

                        .requestMatchers("/api/v1/trainees/**").hasAnyAuthority(Roles.TRAINEE.toString(), Roles.ADMIN.toString())
                        .requestMatchers("/api/v1/trainers/**").hasAnyAuthority(Roles.TRAINER.toString(), Roles.ADMIN.toString())
                        .requestMatchers("/api/v1/trainings/**").hasAuthority(Roles.ADMIN.toString())

                        .anyRequest().authenticated())

                .addFilterBefore(bruteForceProtectionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                                                            loginAttemptService,
                                                            jwtUtil),
                                 UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                                                throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager,
                                                                 LoginAttemptService loginAttemptService,
                                                                 JwtUtil jwtUtil){
        return new CustomAuthenticationFilter(authenticationManager, loginAttemptService, jwtUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
