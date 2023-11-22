package com.betting.ground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "Authorization", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(servers = {
	@Server(url = "/", description = "Default Server URL")}, info = @Info(title = "Betting Ground", version = "1.0.0"), security = {
	@SecurityRequirement(name = "Authorization")})
@SpringBootApplication
public class GroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroundApplication.class, args);
	}

}
