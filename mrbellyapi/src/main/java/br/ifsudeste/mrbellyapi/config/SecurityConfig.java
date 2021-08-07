package br.ifsudeste.mrbellyapi.config;

import br.ifsudeste.mrbellyapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginService service;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/contratos")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/enderecos/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/fiadores/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/imoveis/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/locadores/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/locatarios/**")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/logins/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        ;
    }
}
