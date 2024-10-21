package nl.qnh.qforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Ilionx QForce",
        version = "1.0",
        description = "This API will return Star Wars characters"
    )
)
public class QforceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QforceApplication.class, args);
	}

}
