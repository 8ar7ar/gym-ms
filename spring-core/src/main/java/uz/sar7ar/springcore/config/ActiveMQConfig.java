package uz.sar7ar.springcore.config;


import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ActiveMQConfig {
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestinationName("defaultQueue");  // Set default destination here
        return jmsTemplate;
    }


//    @Bean
//    public MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);  // Send messages as plain text (JSON)
//        converter.setTypeIdPropertyName("_type");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        converter.setObjectMapper(objectMapper);
//
//        return converter;
//    }
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        // Provide the ActiveMQ broker URL
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//        factory.setBrokerURL("tcp://localhost:61616");
//        return factory;
//    }

//    @Bean
//    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
//        return new JmsTemplate(connectionFactory);
//    }
}
