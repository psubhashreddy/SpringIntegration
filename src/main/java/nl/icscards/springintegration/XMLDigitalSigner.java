package nl.icscards.springintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("/config/integration-context.xml")
public class XMLDigitalSigner {

	public static void main(String[] args) {
		SpringApplication.run(XMLDigitalSigner.class, args);
	}
}