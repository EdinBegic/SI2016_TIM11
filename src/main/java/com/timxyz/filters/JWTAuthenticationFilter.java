package com.timxyz.filters;

import com.timxyz.services.TokenAuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.timxyz.filters.JWTLoginFilter.ALLOWED_ORIGIN;
import static com.timxyz.filters.JWTLoginFilter.HEADER_CORS;

public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {

        try {
            Authentication authentication = TokenAuthenticationService
                    .getAuthentication((HttpServletRequest) request);

            if (authentication == null) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ((HttpServletResponse) response).addHeader(HEADER_CORS, ALLOWED_ORIGIN);
            } else {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) response).addHeader(HEADER_CORS, ALLOWED_ORIGIN);
        }
    }
}