package uz.sar7ar.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The main class for the Eureka Server application.
 * This class is responsible for starting the Eureka Server and enabling the Eureka Server functionality.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
