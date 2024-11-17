package uz.sar7ar.springcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJms
@EnableFeignClients(basePackages = "uz.sar7ar.springcore.mscomunication")
public class SpringCoreApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringCoreApplication.class, args);
    }
}
