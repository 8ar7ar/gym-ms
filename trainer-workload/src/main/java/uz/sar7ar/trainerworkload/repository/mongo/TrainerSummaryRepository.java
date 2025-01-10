package uz.sar7ar.trainerworkload.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import uz.sar7ar.trainerworkload.model.mongodb.TrainersTrainingSummary;

import java.util.Optional;

@Service
public interface TrainerSummaryRepository
         extends MongoRepository<TrainersTrainingSummary, String>{

    Optional<TrainersTrainingSummary> findByUserName(String trainerUsername);
}
