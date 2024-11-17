package uz.sar7ar.trainerworkload.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import uz.sar7ar.trainerworkload.model.mongodb.TrainersTrainingSummary;

import java.util.Optional;

/**
 * Repository for managing {@link TrainersTrainingSummary} entities.
 */
@Service
public interface TrainerSummaryRepository
         extends MongoRepository<TrainersTrainingSummary, String>{

    /**
     * Find a trainer's training summary by the trainer's username.
     *
     * @param trainerUsername the trainer's username.
     * @return an optional containing the trainer's training summary if found.
     */
    Optional<TrainersTrainingSummary> findByUserName(String trainerUsername);
}
