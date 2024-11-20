package uz.sar7ar.trainerworkload.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * Configuration class for ActiveMQ.
 * Enables JMS (Java Message Service) support in the application.
 */
@Configuration
@EnableJms
@Slf4j
public class ActiveMQConfig {
    public ActiveMQConfig() {
        log.info("Initializing ActiveMQ configuration");
    }
}
