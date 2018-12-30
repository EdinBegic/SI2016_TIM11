package com.timxyz.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timxyz.models.Account;
import com.timxyz.models.Credentials;
import com.timxyz.repositories.AccountRepository;
import com.timxyz.services.TokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    static final String HEADER_CORS = "Access-Control-Allow-Origin";
    static final String ALLOWED_ORIGIN = "*";
    private static AccountRepository accountRepository;

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
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(creds.getUsername(),creds.getPassword(),
                grantedAuthorities);

        return getAuthenticationManager().authenticate(upToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        TokenAuthenticationService.addAuthentication(req, res, auth.getName());
    }
}