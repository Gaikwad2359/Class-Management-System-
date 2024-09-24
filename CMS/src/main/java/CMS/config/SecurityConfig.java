package CMS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public CustomAuthSucessHandler successHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/", "/register", "/signin", "/saveUser", "/saveAdmin", "/verify", "/homePage").permitAll()  // Allow /homePage without authentication
                .requestMatchers("/guest/**").permitAll()  // Allow all guest pages to be accessed without authentication
                .requestMatchers("/images/**", "/css/**", "/js/**" ,"/pdf/**").permitAll()  // Allow static resources
                .anyRequest().authenticated()  // All other URLs require authentication
            .and()
            .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/studentLogin")
                .successHandler(successHandler)
                .permitAll()
            .and()
            .logout()
                .permitAll();

        return http.build();
    }


}
