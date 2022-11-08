
package com.example.demo.security.utils;

import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.security.jwt.CustomAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;


    public WebSecurityConfig(UserDetailsService userDetailsService ) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
    }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }


    @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception{
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManagerBean());
    customAuthFilter.setFilterProcessesUrl("/api/auth/signin");

    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests().antMatchers("api/auth/signin/**","api/auth/token/refresh/**").permitAll();
    http.authorizeRequests().antMatchers("api/profil/**").hasAnyAuthority("ROLE_USE","ROLE_ADMIN");
    http.addFilter(customAuthFilter);

    http.addFilterBefore(new AuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }




}

