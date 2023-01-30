package com.example.demo.security.utils;

import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.security.jwt.CustomAuthFilter;
import com.example.demo.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
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

import javax.servlet.http.HttpServletResponse;



@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthTokenFilter authTokenFilter;


    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static final String[] AUTH_WHITELIST = {
            "/authenticate",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/webjars/**",
            "/api/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManagerBean());
         customAuthFilter.setFilterProcessesUrl("/api/auth/signin");

        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();
        // Set permissions on endpoints
      http.authorizeRequests()
              .antMatchers("/api/**").permitAll()
           //   .antMatchers("/api/**").hasAnyAuthority(ERole.ROLE_USER.name(),ERole.ROLE_ADMIN.name())
           //   .antMatchers("/api/**").hasAnyAuthority(ERole.ROLE_USER.name(),ERole.ROLE_ADMIN.name())
              .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilter(customAuthFilter);
        http.addFilterBefore(new AuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }


}

