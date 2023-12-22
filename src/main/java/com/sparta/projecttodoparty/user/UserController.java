package com.sparta.projecttodoparty.user;


import com.sparta.projecttodoparty.common.CommonResponseDto;
import com.sparta.projecttodoparty.jwt.JwtUtil;
import com.sparta.projecttodoparty.user.dto.UserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;

	@Operation(summary = "회원 가입", description = "회원 가입")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "2xx", description = "회원 가입 성공", useReturnTypeSchema = true)
	})
	@PostMapping("/signup")
	public ResponseEntity<Object> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
		try {
			userService.singup(userRequestDto);
		} catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(new CommonResponseDto("사용할 수 없는 아이디 입니다."
							, HttpStatus.BAD_REQUEST.value()));
		}

		return ResponseEntity.status(HttpStatus.CREATED.value())
				.body(new CommonResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
	}

	@Operation(summary = "로그인", description = "로그인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "2xx", description = "로그인 성공")
	})
	@PostMapping("/login")
	public ResponseEntity<CommonResponseDto> login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
		try {
			userService.login(userRequestDto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest()
					.body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}

		response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userRequestDto.getUserAccount()));

		return ResponseEntity.ok().body(new CommonResponseDto("로그인 성공", HttpStatus.OK.value()));
	}
}
