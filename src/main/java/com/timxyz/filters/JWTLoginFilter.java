package com.timxyz.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import com.timxyz.repositories.AccountRepository;
import com.timxyz.services.TokenAuthenticationService;
import com.timxyz.models.Credentials;
import com.timxyz.models.Account;
import java.util.logging.Logger;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static AccountRepository accountRepository;
    private static Logger logger = Logger.getLogger(JWTLoginFilter.class.getName());
    static final String HEADER_CORS = "Access-Control-Allow-Origin";
    static final String ALLOWED_ORIGIN = "*";

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
        throws AuthenticationException, IOException, ServletException {
        
        res.addHeader(HEADER_CORS, ALLOWED_ORIGIN);

        ServletContext servletContext = req.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        accountRepository = webApplicationContext.getBean(AccountRepository.class);    

        Credentials creds = new ObjectMapper()
            .readValue(req.getInputStream(), Credentials.class);

        Account account = accountRepository.findByUsername(creds.getUsername());

        // precica zbog bcrypt hashiranja passworda 
        if (account != null) {
            if (BCrypt.checkpw(creds.getPassword(), account.getPassword()))
                creds.setPassword(account.getPassword());
            else
                creds.setPassword("");
        }
              
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (account != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
        }

        return getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(
                creds.getUsername(),
                creds.getPassword(),
                grantedAuthorities //Collections.emptyList()
            )
        );
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest req,
        HttpServletResponse res, FilterChain chain,
        Authentication auth) throws IOException, ServletException {       
        TokenAuthenticationService.addAuthentication(req, res, auth.getName());
    }
}