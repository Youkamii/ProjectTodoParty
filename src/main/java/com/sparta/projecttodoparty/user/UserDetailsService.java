package com.sparta.projecttodoparty.user;

import com.sparta.projecttodoparty.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsImpl getUserDetails(String userAccount) {
		User user = userRepository.findByUserAccount(userAccount)
				.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자" + userAccount));
		return new UserDetailsImpl(user);
	}
}
