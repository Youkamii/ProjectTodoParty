package com.sparta.projecttodoparty.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String userAccount;

	@Column(nullable = false)
	private String password;

	public User(String userAccount, String password) {
		this.userAccount = userAccount;
		this.password = password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
