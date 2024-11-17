package com.br.contas.apagar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Contas a Pagar API", version = "1.0", description = "API para gerenciamento de contas a pagar"))
@SpringBootApplication
public class ContasApagarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContasApagarApplication.class, args);
	}

}
