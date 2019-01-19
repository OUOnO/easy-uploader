package com.shujie.uploader.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p> description: TODO
 * <p> 2019/01/19
 *
 * @author ssj
 * @version 1.0.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/download").permitAll()
            .antMatchers("/jquery/**").permitAll()
            .antMatchers("/bootstrap/**").permitAll()
            .antMatchers("/download/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic().and()
            .csrf().disable();
    }

}
