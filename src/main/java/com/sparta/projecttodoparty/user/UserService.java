package com.sparta.projecttodoparty.user;

import com.sparta.projecttodoparty.user.dto.UserRequestDto;
import com.sparta.projecttodoparty.user.repository.UserRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public void	singup(UserRequestDto requestDto) {
		String userAccount = requestDto.getUserAccount();
		String password = passwordEncoder.encode(requestDto.getPassword());

		if (userRepository.findByUserAccount(userAccount).isPresent()) {
			throw new IllegalArgumentException("사용할 수 없는 아이디 입니다.");
		}

		User user = new User(userAccount, password);
		userRepository.save(user);
	}

	public void login(UserRequestDto userRequestDto) {
		String userAccount = userRequestDto.getUserAccount();
		String password = userRequestDto.getPassword();

		User user = userRepository.findByUserAccount(userAccount)
				.orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
	}

	public void logout() {
		// 로그아웃 처리 로직 구현
	}

	public void changePassword(String username, String newPassword) {
		User user = userRepository.findByUserAccount(username)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	public UserDetails getUserDetails(String userAccount) {
		User user = userRepository.findByUserAccount(userAccount)
				.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자" + userAccount));
		return new UserDetailsImpl(user);
	}
}



