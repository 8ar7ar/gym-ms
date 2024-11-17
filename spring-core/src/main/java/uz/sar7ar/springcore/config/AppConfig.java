package uz.sar7ar.springcore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

/**
 * Configuration class for the application.
 * This class is responsible for configuring the application context and handling context refresh events.
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "uz.sar7ar.springcore")
public class AppConfig {
    static { log.info("Initializing configuration: {}", AppConfig.class); }

    /**
     * Handle the context refresh event.
     * <p>
     * This method is an event listener for the context refresh event.
     * It is responsible for logging all the mapped handlers and their corresponding request mappings.
     * <p>
     * The method obtains the application context from the event, and then uses the application context
     * to obtain the bean of type {@link RequestMappingHandlerMapping}.
     * It then uses the {@link RequestMappingHandlerMapping} to obtain a map of the request mappings and
     * their corresponding handler methods. Finally, it iterates over the map and logs each entry.
     *
     * @param event The context refresh event.
     */
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                applicationContext.getBean("requestMappingHandlerMapping",
                                           RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map =
                requestMappingHandlerMapping.getHandlerMethods();
        map.forEach((key, value) -> log.info("{} {}", key, value));
    }
}
