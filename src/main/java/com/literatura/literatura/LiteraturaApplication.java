package com.literatura.literatura;

import com.literatura.literatura.model.Datos;
import com.literatura.literatura.principal.Principal;
import com.literatura.literatura.service.ConsumoApi;
import com.literatura.literatura.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.mostrarMenu();
	}
}
