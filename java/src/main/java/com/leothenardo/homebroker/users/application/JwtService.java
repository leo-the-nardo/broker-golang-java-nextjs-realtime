package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._common.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtService {

	@Value("${access-token.secret}")
	private String SECRET;

	@Value("${access-token.expiration-minutes}")
	private long EXPIRES_MINUTES;


	public String extractUserId(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts
							.parserBuilder()
							.setSigningKey(getSignKey())
							.build()
							.parseClaimsJws(token)
							.getBody();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new UnauthorizedException();
		}
	}


	public String generateToken(String userId) {
		//ONLY PUT PUBLIC DATA IN THE CLAIMS
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userId);
	}

	private String createToken(Map<String, Object> claims, String id) {

		return Jwts.builder()
						.setClaims(claims)
						.setSubject(id)
						.setIssuedAt(new Date(System.currentTimeMillis()))
						.setExpiration(new Date(System.currentTimeMillis() + (EXPIRES_MINUTES * 60 * 1000)))
						.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	//	public String extractUsername(String token) {
	//		return extractClaim(token, claims -> claims.get("email", String.class));
	//	}

}
