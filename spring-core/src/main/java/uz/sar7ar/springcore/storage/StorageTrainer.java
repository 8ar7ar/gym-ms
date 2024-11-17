package uz.sar7ar.springcore.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.sar7ar.springcore.model.pojo_models.Trainer;
import uz.sar7ar.springcore.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StorageTrainer {
    @Getter
    private final Map<Integer, Trainer> trainers = new HashMap<>();
    private final String trainersFileLocation;

    @Autowired
    public StorageTrainer(@Value("${data.trainers}") String trainersFileLocation) {
        this.trainersFileLocation = trainersFileLocation;
    }

    @PostConstruct
    public void init() throws IOException {
        log.info("Initializing Trainer storage and filling with data from external file...");
        ObjectMapper mapper = new ObjectMapper();
        List<Trainer> trainerList = mapper.readValue(Files.readAllBytes(Path.of(trainersFileLocation)),
                new TypeReference<>() {
                });
        trainerList.forEach(trainer -> trainers.put(trainer.getTrainerId(), trainer));
        trainers.values().forEach(trainer -> {
                        trainer.setUsername(Utils.generateUserName(trainer.getFirstName(), trainer.getLastName()));
                        trainer.setPassword(Utils.generateRandomPassword());});
        log.info("Trainer storage initialization finished");
    }

    @PreDestroy
    public void destroy() {
        trainers.clear();
    }
}
