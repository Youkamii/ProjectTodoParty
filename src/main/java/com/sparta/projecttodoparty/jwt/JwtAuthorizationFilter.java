package com.sparta.projecttodoparty.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.projecttodoparty.common.CommonResponseDto;
import com.sparta.projecttodoparty.user.UserDetailsImpl;
import com.sparta.projecttodoparty.user.UserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final UserDetailsService userDetailsService;



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtUtil.resolveToken(request);

		if (Objects.nonNull(token)) {
			if (jwtUtil.validateToken(token)) {
				Claims info = jwtUtil.getUserInfoFromToken(token);

				String username = info.getSubject();
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UserDetailsImpl userDetails = userDetailsService.getUserDetails(username);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				context.setAuthentication(authentication);
				SecurityContextHolder.setContext(context);

			} else {

				CommonResponseDto commonResponseDto = new CommonResponseDto("유효하지 않은 토큰", HttpStatus.BAD_REQUEST.value());
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));

				return;
			}
		}

		filterChain.doFilter(request, response);
	}

}
