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
import uz.sar7ar.springcore.model.pojo_models.Training;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StorageTraining {
    @Getter
    private final Map<String, Training> trainings = new HashMap<>();
    private final String trainingsFileLocation;

    @Autowired
    public StorageTraining(@Value("${data.training}") String trainingsFileLocation) {
        this.trainingsFileLocation = trainingsFileLocation;
    }

    @PostConstruct
    public void init() throws IOException {
        log.info("Initializing Training storage and filling with data from external file...");

        ObjectMapper mapper = new ObjectMapper();
        List<Training> trainingList = mapper.readValue(Files.readAllBytes(Path.of(trainingsFileLocation)),
                                                       new TypeReference<>() {});
        trainingList.forEach(training -> trainings.put(training.getTrainingName(), training));

        log.info("Training storage initialization finished");
    }

    @PreDestroy
    public void destroy() {
        trainings.clear();
    }
}
