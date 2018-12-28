package com.timxyz.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;
import javax.json.Json;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.io.IOException;
import com.timxyz.repositories.AccountRepository;
import com.timxyz.models.Account;

public class TokenAuthenticationService {

    private static AccountRepository accountRepository;

    static final long EXPIRATIONTIME = 7_200_000; // 2 hours
    static final String SECRET = "TSProject2018";
    static final String TOKEN_PREFIX = "Bearer";
    static final String FIELD_NAME_TOKEN = "jwt";
    static final String FIELD_NAME_ROLE = "role";
    static final String FIELD_NAME_USERNAME = "username";
    static final String HEADER_STRING = "Authorization";

    public static Account findAccountByToken(String token) {
        String username = parseJwt(token);

        if (username == null) {
            return null;
        }

        return accountRepository.findByUsername(username);
    }
    
    public static String parseJwt(String token) {
    	if (token != null) {
    		return Jwts.parser()
    			.setSigningKey(SECRET)
    			.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
    			.getBody()
    			.getSubject();
    	}
    	
    	return null;
    }

    //ovdje cemo morati dodati uloge u odgovor da bi na klijentskoj strani znali privilegije
    public static void addAuthentication(HttpServletRequest req,
        HttpServletResponse res, String username) throws IOException{
        //ovaj dio sam dodao zbor neuspjelog Dependancy Injection (AccountRepository)
        if (accountRepository == null) {
            ServletContext servletContext = req.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            accountRepository = webApplicationContext.getBean(AccountRepository.class);
        }
        Account account = accountRepository.findByUsername(username);
        String JWT = Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
            
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
        	.add(FIELD_NAME_USERNAME, username)
            .add(FIELD_NAME_TOKEN, JWT)
            .add(FIELD_NAME_ROLE, account.getRole().getName());

        JsonObject responseObj = objectBuilder.build();
        res.getWriter().write(responseObj.toString());
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        accountRepository = webApplicationContext.getBean(AccountRepository.class);

        String userReq = parseJwt(request.getHeader(HEADER_STRING));

        Account account = accountRepository.findByUsername(userReq);

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (account != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
        }
        else {
            userReq = null;
        }

        return userReq != null ? new UsernamePasswordAuthenticationToken(userReq, null, grantedAuthorities) : null;
    }
}