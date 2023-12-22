package com.sparta.projecttodoparty.user.repository;

import com.sparta.projecttodoparty.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserAccount(String username);

}
