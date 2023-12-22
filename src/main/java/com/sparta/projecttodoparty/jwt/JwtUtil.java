package com.sparta.projecttodoparty.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String BEARER_PREFIX = "Bearer ";

	@Value("${jwt.secret.key}")
	private String secretKey;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	private Key key;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않은 JWT 서명", e);
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT 토큰", e);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원하지 않는 JWT 토큰", e);
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token, 잘못된 구조의 JWT 토큰", e);
		} catch (Exception e) {
			log.error("JWT token validation error, JWT 토큰 검증 오류", e);
		}
		return false;
	}

	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String createToken(String userAccount) {
		Date date = new Date();

		long TOKEN_TIME = 60 * 60 * 1000;
		return BEARER_PREFIX +
				Jwts.builder()
						.setSubject(userAccount)
						.setExpiration(new Date(date.getTime() + TOKEN_TIME))
						.setIssuedAt(date)
						.signWith(key, signatureAlgorithm)
						.compact();
	}
}
