package com.platform.ticket.ticket_platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(requests -> requests
                //Accessi riservati
                .requestMatchers("/operators/**").hasAuthority("Operatore")
                .requestMatchers("/admin/**").hasAuthority("Admin")
                .requestMatchers("/tickets/create" , "/tickets/delete/**").hasAuthority("Admin")
                .requestMatchers("/users/**").hasAuthority("Admin")

                //Accessi condivisi
                .requestMatchers("/notes/**").hasAnyAuthority("Admin", "Operatore")
                .requestMatchers("/tickets/edit/**", "/tickets/{id}").hasAnyAuthority("Admin", "Operatore")

                //Accessi pubblici
                .requestMatchers("/home/**","/css/**","/img/**","/js/**","/webjars/**").permitAll()
                .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                    .accessDeniedPage("/error/403")
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
                )
                .logout(logout -> logout
                .logoutSuccessUrl("/?logout")
                .permitAll()
                );
        return http.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
