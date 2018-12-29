package com.timxyz;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;


import com.timxyz.filters.JWTLoginFilter;
import com.timxyz.filters.JWTAuthenticationFilter;
import com.timxyz.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_FINANCE = "FINANCE";
    private final String ROLE_AUDIT_TEAM = "AUDITTEAM";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired 
    private SecurityProperties securityProperties;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (securityProperties.isRequireSsl()) http.requiresChannel().anyRequest().requiresSecure();

        http.csrf().disable().authorizeRequests()
            //.antMatchers(HttpMethod.GET,"/accounts/**").authenticated()
            .antMatchers(HttpMethod.GET, "/index.html").permitAll()
            .antMatchers(HttpMethod.GET,"/audits/**", "/items/**", "/locations/**", "/categories/all").authenticated()
            .antMatchers("/accounts/**").hasRole(ROLE_ADMIN)
            .antMatchers(HttpMethod.DELETE, "/audits/**").hasRole(ROLE_ADMIN)
            .antMatchers("/audits/**").hasAnyRole(ROLE_ADMIN,ROLE_AUDIT_TEAM)
                // SRS FZ17-18
                // Korisnik sa administratorskim privilegijama ili korisnik finansijske službe može
                // kreirati/obrisati novu prostoriju #LOL
            .antMatchers("/locations/**").hasAnyRole(ROLE_ADMIN,ROLE_FINANCE)
            .antMatchers(HttpMethod.GET,"/locationTypes/**").hasAnyRole(ROLE_ADMIN)
            .antMatchers("/locationTypes/**").hasRole(ROLE_ADMIN)
            .antMatchers("/categories/**").hasRole(ROLE_ADMIN)
            .antMatchers(HttpMethod.GET,"/status/**").hasAnyRole(ROLE_ADMIN, ROLE_AUDIT_TEAM)
            .antMatchers("/status/**").hasRole(ROLE_ADMIN)
            .antMatchers("/items/**").hasAnyRole(ROLE_ADMIN, ROLE_AUDIT_TEAM)
            .antMatchers("/access-logs/**").hasAnyRole(ROLE_ADMIN, ROLE_FINANCE)
            .antMatchers("/reports/**").hasAnyRole(ROLE_ADMIN, ROLE_FINANCE)
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            // filtriramo sve zahtjeve za login sa nasim filterom JWTLoginFilter
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
            // sve ostale zahtjeve filtriramo sa nasim filterom JWTAuthenticationFilter
            .addFilterBefore(new JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    //CORS problemi - potrebno je dozvoliti da OPTIONS zahtjevi koje browser salje
    //stignu do servera
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring().antMatchers("/index.html", "/*.js", "/*.css", "/");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
       /* auth.inMemoryAuthentication()
            .withUser("admin")
            .password("adminpass")
            .roles("ADMIN");*/
    }
}