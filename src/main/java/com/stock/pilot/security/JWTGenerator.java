package com.stock.pilot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTGenerator {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration; // milliseconds

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(UserDetails userDetails) {

		String token = Jwts.builder()
				.subject(userDetails.getUsername())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey())
				.compact();

		System.out.println("New token:");
		System.out.println(token);

		return token;
	}

	public String getUsername(String token) {

		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	public boolean validateToken(String token, UserDetails userDetails) {

		try {
			String username = getUsername(token);

			return username.equals(userDetails.getUsername())
					&& !isTokenExpired(token);

		} catch (Exception e) {
			return false;
		}
	}

	private boolean isTokenExpired(String token) {

		Date expirationDate = Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration();

		return expirationDate.before(new Date());
	}
}
