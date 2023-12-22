package com.sparta.projecttodoparty.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

	@Pattern(regexp = "^[a-z0-9]{12}$")
	private String userAccount;

	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{1,16}$")
	private String password;
}
