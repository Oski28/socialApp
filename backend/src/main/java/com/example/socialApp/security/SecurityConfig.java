package com.example.socialApp.security;

import com.example.socialApp.security.jwt.AuthEntryPointJwt;
import com.example.socialApp.security.jwt.AuthTokenFilter;
import com.example.socialApp.security.services.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] URLS = {
            "/api/users/**",
            "/api/categories/**",
            "/api/chats/**",
            "/api/events/**",
            "/api/notices/**",
            "/api/messages/**",
            "/api/reports/**",
            "/api/requestToJoin/**"
    };

    private final String[] CSRF_IGNORE = {
            "/api/auth/signin/**", "/api/auth/signup", "/api/auth/confirm-account/**"
    };

    private UserDetailsServiceImplementation userDetailsService;

    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImplementation userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUnauthorizedHandler(AuthEntryPointJwt unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(CSRF_IGNORE)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("api/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .antMatchers(URLS).authenticated();

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
