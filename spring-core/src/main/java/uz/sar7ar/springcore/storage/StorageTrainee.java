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
import uz.sar7ar.springcore.model.pojo_models.Trainee;
import uz.sar7ar.springcore.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StorageTrainee {
    @Getter
    private final Map<Integer, Trainee> trainees = new HashMap<>();
    private final String TRAINEES_DATA_LOCATION;

    @Autowired
    public StorageTrainee(@Value("${data.trainees}") String trainees_data_path) {
        this.TRAINEES_DATA_LOCATION = trainees_data_path;
    }

    @PostConstruct
    public void init() throws IOException {
        log.info("Initializing Trainee storage and filling with data from external file...");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Trainee>> typeReference = new TypeReference<>() {};

        List<Trainee> traineeList = mapper.readValue(Files.readAllBytes(Path.of(TRAINEES_DATA_LOCATION)), typeReference);
        traineeList.forEach(trainee -> trainees.put(trainee.getTraineeId(), trainee));
        trainees.values().forEach(trainee -> {
                            trainee.setUsername(Utils.generateUserName(trainee.getFirstName(), trainee.getLastName()));
                            trainee.setPassword(Utils.generateRandomPassword());});
        log.info("Trainee storage initialization finished");
    }

    @PreDestroy
    public void destroy() {
        trainees.clear();
    }
}
