package me.franciscomolina.back_portal_empleo_mayor50;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackPortalEmpleoMayor50Application {

	public static void main(String[] args) {
		SpringApplication.run(BackPortalEmpleoMayor50Application.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){//Encriptamos las contraseñas de los usuarios en la base de datos
		return new BCryptPasswordEncoder();
	}

}
