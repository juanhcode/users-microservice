package com.develop.users_microservice;

import com.develop.users_microservice.domain.model.Role;
import com.develop.users_microservice.domain.model.User;
import com.develop.users_microservice.infrastructure.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = UsersMicroserviceApplication.class)
class UsersMicroserviceApplicationTests {
	@Autowired
	private UserRepositoryImpl userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void deleteUser() {
		Role role = new Role(1L, "ROLE_USER");

		User user = new User(1L, "juan", "hoyos", "hoyos@gmail.com", "1234", role, "cra34", "token",true );
		User user2 = new User(2L, "Diana", "hoyos", "hoyos@gmail.com", "1234", role, "cra34", "token",true );

		List<User> users = List.of(user, user2);
		userRepository.deleteById(user.getId());
		System.out.println(user);
	}

}
