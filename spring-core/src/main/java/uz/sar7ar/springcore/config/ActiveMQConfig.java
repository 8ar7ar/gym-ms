package uz.sar7ar.springcore.config;


import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configuration class for setting up ActiveMQ.
 */
@Configuration
public class ActiveMQConfig {

    /**
     * JmsTemplate bean for sending and receiving messages to/from ActiveMQ.
     * <p>
     * This bean is required for the {@link org.springframework.jms.core.JmsMessagingTemplate} to work.
     * <p>
     * The default destination name is set to "defaultQueue".
     *
     * @param connectionFactory the JMS connection factory
     * @return the JmsTemplate instance
     */
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
