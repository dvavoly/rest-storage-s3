package org.example.storage.config;

import org.example.storage.model.enums.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/files/**").hasAuthority(Permission.FILES_READ.getPermission())
                .antMatchers(HttpMethod.POST, "/api/v1/files/**").hasAuthority(Permission.FILES_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority(Permission.USERS_READ.getPermission())
                .antMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority(Permission.USERS_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/api/v1/events/**").hasAuthority(Permission.EVENTS_READ.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasAuthority(Permission.EVENTS_DELETE.getPermission())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider authenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
