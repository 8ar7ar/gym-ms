package uz.sar7ar.trainerworkload.messageconsumer;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import uz.sar7ar.trainerworkload.service.TrainerService;

import java.time.LocalDate;

/**
 * This class is responsible for consuming messages from a JMS queue.
 * It listens to the "defaultQueue" and processes incoming messages.
 */
@Slf4j
@AllArgsConstructor
@Component
public class MsgConsumer {
    private final TrainerService trainerService;

    /**
     * Processes incoming JMS messages from the "defaultQueue".
     * Extracts properties from the message and invokes the trainerService
     * to process the training details.
     *
     * @param message the JMS message containing training details
     * @throws JMSException if there is an issue with message extraction
     */
    @JmsListener(destination = "defaultQueue")
    public void processMessage(Message message) throws JMSException {
        log.info("processing message from defaultQueue: {}", message);
        trainerService.processTraining(
                message.getStringProperty("userName"),
                message.getStringProperty("firstName"),
                message.getStringProperty("lastName"),
                message.getBooleanProperty("isActive"),
                LocalDate.parse(message.getStringProperty("trainingDate")),
                message.getIntProperty("durationInHours"),
                message.getStringProperty("actionType"));
        log.info("message processed: {}", message);
        System.out.println(message);
    }
}
