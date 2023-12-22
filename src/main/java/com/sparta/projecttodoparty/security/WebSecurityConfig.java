package com.sparta.projecttodoparty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.projecttodoparty.jwt.JwtAuthorizationFilter;
import com.sparta.projecttodoparty.jwt.JwtUtil;
import com.sparta.projecttodoparty.user.UserDetailsService;
import com.sparta.projecttodoparty.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;

	private final UserDetailsService userDetailsService;

	private final UserService userService;

	private final ObjectMapper objectMapper;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		return new JwtAuthorizationFilter(jwtUtil, objectMapper, userDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable);

		http.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests((authorizeHttpRequests) ->
				authorizeHttpRequests
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
								.requestMatchers("api/user/**").permitAll()
						.anyRequest().authenticated()
		);

		http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

}
