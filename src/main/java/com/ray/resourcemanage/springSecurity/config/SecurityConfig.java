package com.ray.resourcemanage.springSecurity.config;

import com.ray.resourcemanage.springSecurity.Handler.MyAuthenticationFailHandler;
import com.ray.resourcemanage.springSecurity.Handler.MyAuthenticationSuccessHandler;
import com.ray.resourcemanage.springSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationFailHandler myAuthenticationFailHandler;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Bean
    UserDetailsService userService() {
        return new UserService();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginProcessingUrl("/login")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailHandler)
                .and().csrf().disable();
    }
}
