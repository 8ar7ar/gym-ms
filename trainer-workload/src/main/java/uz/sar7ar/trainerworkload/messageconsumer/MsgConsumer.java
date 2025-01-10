package uz.sar7ar.trainerworkload.messageconsumer;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import uz.sar7ar.trainerworkload.service.TrainerService;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class MsgConsumer {
    private final TrainerService trainerService;

    @JmsListener(destination = "defaultQueue")
    public void processMessage(Message message) throws JMSException {
        trainerService.processTraining(
                message.getStringProperty("userName"),
                message.getStringProperty("firstName"),
                message.getStringProperty("lastName"),
                message.getBooleanProperty("isActive"),
                LocalDate.parse(message.getStringProperty("trainingDate")),
                message.getIntProperty("durationInHours"),
                message.getStringProperty("actionType"));
        System.out.println(message);
    }
}
