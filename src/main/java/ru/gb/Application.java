package ru.gb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.gb.model.security.User;
import ru.gb.repository.UserRepository;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		UserRepository repository = context.getBean(UserRepository.class);
		repository.save(new User("igor","pas"));
	}


}