package uz.sar7ar.trainerworkload.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.sar7ar.trainerworkload.model.TrainingSession;
import uz.sar7ar.trainerworkload.model.Trainer;
import uz.sar7ar.trainerworkload.repository.TrainerRepository;
import uz.sar7ar.trainerworkload.repository.TrainingSessionRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing trainer-related operations.
 */
@AllArgsConstructor
@Service
public class TrainerService {
    private TrainerRepository trainerRepository;
    private TrainingSessionRepository trainingSessionRepository;
    /**
     * Process a training event, either adding a new training session or deleting an existing one.
     *
     * @param userName the username of the trainer
     * @param firstName the first name of the trainer
     * @param lastName the last name of the trainer
     * @param isActive the active status of the trainer
     * @param trainingDate the date of the training
     * @param duration the duration of the training in hours
     * @param actionType the type of action to be performed, either "ADD" or "DELETE"
     */
    public void processTraining(String userName, String firstName, String lastName,
                                boolean isActive, LocalDate trainingDate, int duration, String actionType) {
        Optional<Trainer> trainer = trainerRepository.findByUserName(userName);
        if (trainer.isEmpty()) trainer =  Optional.of(new Trainer(userName, firstName, lastName, isActive));

        if ("ADD".equals(actionType)) {
            TrainingSession record = new TrainingSession();
            record.setTrainingDate(trainingDate);
            record.setTrainingDuration(duration);
            record.setTrainer(trainer.get());
            trainerRepository.save(trainer.get());
            trainingSessionRepository.save(record);
            System.out.println("================================================================");
            System.out.println(record);
            System.out.println("================================================================");

        } else if ("DELETE".equals(actionType)) {
            List<TrainingSession> trainingSessions = trainer.get().getTrainingSessions();
            trainingSessions.removeIf(s -> s.getTrainingDate().equals(trainingDate) && s.getTrainingDuration() == duration);
            trainingSessionRepository.deleteAll(trainingSessions);
        }
    }

    /**
     * Get the training summary for a given trainer.
     * <p>
     * The summary is a map of years to maps of months to the total duration of all training sessions in that month.
     * @param username the username of the trainer
     * @return a map of years to maps of months to training durations
     * @throws RuntimeException if the trainer does not exist
     */
    public Map<Integer, Map<Integer, Integer>> getTrainingSummary(String username) {
        Trainer trainer = trainerRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        Map<Integer, Map<Integer, Integer>> yearlyMonthlySummary = new HashMap<>();
        for (TrainingSession session : trainer.getTrainingSessions()) {
            yearlyMonthlySummary
                    .computeIfAbsent(session.getTrainingDate().getYear(), k -> new HashMap<>())
                    .merge(session.getTrainingDate().getMonthValue(), session.getTrainingDuration(), Integer::sum);
        }
        return yearlyMonthlySummary;
    }
}
