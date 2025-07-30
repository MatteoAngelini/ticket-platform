package com.platform.ticket.ticket_platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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

                //Accessi condivisi
                .requestMatchers("/notes/**").hasAnyAuthority("Admin", "Operatore")
                .requestMatchers("/tickets/edit/**").hasAnyAuthority("Admin", "Operatore")

                //Accessi pubblici
                .requestMatchers("/login","/css/**","/img/**","/js/**","/webjars/**").permitAll()
                .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                    .accessDeniedPage("/error/403")
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
                )
                .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
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
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
