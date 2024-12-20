package uni.plovdiv.online_library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import uni.plovdiv.online_library.jpa.UserRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll() // Permit access to auth and static resources
                        .requestMatchers("/index").hasAuthority("ROLE_USER")
                        .requestMatchers("/admin-panel").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .formLogin(form -> form.loginPage("/login").successHandler(authenticationSuccessHandler()))
                .authenticationManager(authenticationManager(userRepository, passwordEncoder()))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new CustomAuthManager(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
