package com.example.demo;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SsJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsJwtApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner start(AccountService accountService ) {
		return args ->{
			accountService.addNewRole(new AppRole(null,"USER"));
			accountService.addNewRole(new AppRole(null,"ADMIN"));
			accountService.addNewRole(new AppRole(null,"CUSTOMER_MANAGER"));
			accountService.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
			accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));
			
			accountService.addNewUser(new AppUser(null,"user1","123",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"admin","123",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user2","123",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user3","123",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user4","123",new ArrayList<>()));
			
			accountService.addRoleToUser("user1", "USER");
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
			accountService.addRoleToUser("user4", "USER");
			accountService.addRoleToUser("user4", "BILLS_MANAGER");
			
		};
	}

}
