package com.zsgs.bankapi.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zsgs.bankapi.config.security.JwtUtilPak;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWtFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtilPak jwtUtilPak;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		if (requestURI.equals("/api/auth/validate")) {
		    filterChain.doFilter(request, response);
		    return; 
		}
	    
	    String token = null;

	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if ("jwt".equals(cookie.getName())) { 
	                token = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (token == null) {
	        String authHeader = request.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	        }
	    }

	    if (token == null) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    try {
	        String userName = jwtUtilPak.extractUserName(token);
	        
	        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            if (!jwtUtilPak.isTokenExpired(token)) {
	                Object rolesClaim = jwtUtilPak.extractClaim(token, claims -> claims.get("rolrs"));
	                List<GrantedAuthority> authorities = new ArrayList<>();
	                if (rolesClaim != null) {
	                    String rolesStr = rolesClaim.toString();
	                    if (rolesStr.contains("MANAGER")) {
	                        authorities.add(new SimpleGrantedAuthority("MANAGER"));
	                    } else if (rolesStr.contains("USER")) {
	                        authorities.add(new SimpleGrantedAuthority("USER"));
	                    }
	                }
	                
	                UserDetails userDetails = new User(userName, "", authorities);

	                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Filter error: " + e.getMessage());
	    }
	    
	    filterChain.doFilter(request, response);
	}
}
