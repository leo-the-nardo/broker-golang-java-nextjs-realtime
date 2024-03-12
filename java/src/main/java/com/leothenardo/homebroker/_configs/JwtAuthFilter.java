package com.leothenardo.homebroker._configs;

import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
import com.leothenardo.homebroker.users.application.JwtService;
import com.leothenardo.homebroker.users.helpers.CustomUserDetails;
import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Value("${access-token.cookie-name}")
	private String COOKIE_NAME;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = extractToken(request);
		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			validate(request, token);
		}

		filterChain.doFilter(request, response);
	}

	private void validate(HttpServletRequest request, String token) {
		String userId = jwtService.extractUserId(token);
		User user = userRepository.findById(userId).orElseThrow(UnauthorizedException::new);
		var userDetails = new CustomUserDetails(user);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

	}

	private String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		if (request.getCookies() == null) {
			return null;
		}
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(COOKIE_NAME)) {
				System.out.println(COOKIE_NAME + cookie.getValue());
				return cookie.getValue();
			}
		}
		System.out.println("no token found in cookies or headers -------");
		for (Cookie cookie : request.getCookies()) {
			System.out.println("cookie: " + cookie.getName());
		}
		return null;
	}

}
