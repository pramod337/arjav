package com.example.vajra.config;

import com.example.vajra.repository.EmailRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public access for specific URLs
                .requestMatchers(
                    "/home.html", "/login", "/register", "/users/register", "/error", "/error/**",
                    "/send-email", "/sendmail.html", "/static/**", "/css/**", "/js/**", "/distance.html",
                    "/liveloc.html", "/save-otp", "/verify-otp", "/auto.html", "/autocomplete", "/distance",
                    "/images/**", "/cement.html", "/cart.html", "/vendor_register.html", "/vendor-login.html", "/api/home/products","/api/products/all","/api/products/by-vendor","/vendor-products.html",
                    "/api/products/by-vendor-name","/image-upload.html"
                ).permitAll()
                // Access control for authenticated users
                .requestMatchers("/booking.html").authenticated()
                // Only admin users can access
                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                // Only vendor users can access vendor pages
                .requestMatchers("/vendor/**", "/vendor_register.html", "/vendor-login.html" ,"add_products.html").hasAuthority("ROLE_VENDOR")
                // Only user users can access user pages
                .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER")
                .anyRequest().permitAll()
            )
            .formLogin(login -> login
                    .loginPage("/login") // Single login form
                    .loginProcessingUrl("/auth/login")
                    .successHandler((request, response, authentication) -> {
                        var authorities = authentication.getAuthorities();
                        String redirectUrl = "/booking.html"; // Default for normal users

                        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDOR"))) {
                            redirectUrl = "/add_products.html";
                        }

                        response.setContentType("application/json");
                        response.getWriter().write("{\"redirectUrl\": \"" + redirectUrl + "\"}");
                    })
                    .permitAll()
                )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            )
            .httpBasic(); // Enable HTTP Basic authentication if necessary

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(EmailRepository userRepository) {
        return username -> userRepository.findByUsername(username)
            .map(user -> {
                System.out.println("Roles assigned to user: " + user.getRoles());  // Debugging line
                return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
                );
            })
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // Allow requests from this origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow these HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials (e.g., cookies)
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
