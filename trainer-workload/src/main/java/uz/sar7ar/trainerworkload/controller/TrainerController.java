package uz.sar7ar.trainerworkload.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sar7ar.trainerworkload.service.TrainerService;
import uz.sar7ar.trainerworkload.service.TrainerSummaryService;

import java.time.LocalDate;

/**
 * This class is a REST controller for handling trainer-related operations.
 * It provides endpoints for updating training information and retrieving training summaries.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/trainer-workload-service")
public class TrainerController {
    private TrainerService trainerService;
    private TrainerSummaryService trainerSummaryService;

    /**
     * Updates the training information for a specific trainer.
     *
     * @param trainerUsername the username of the trainer
     * @param firstName the first name of the trainer
     * @param lastName the last name of the trainer
     * @param isActive the active status of the trainer
     * @param trainingDate the date of the training session
     * @param durationInHours the duration of the training session in hours
     * @param actionType the type of action to be performed on the training data
     * @return ResponseEntity with HTTP status 200 (OK) upon successful execution
     */
    @PostMapping("/update-training")
    public ResponseEntity<Void> updateTraining(@RequestParam String trainerUsername,
                                            @RequestParam String firstName,
                                            @RequestParam String lastName,
                                            @RequestParam boolean isActive,
                                            @RequestParam LocalDate trainingDate,
                                            @RequestParam int durationInHours,
                                            @RequestParam String actionType) {
        trainerService.processTraining(trainerUsername, firstName, lastName, isActive, trainingDate, durationInHours, actionType);
        trainerSummaryService.processTrainerEvent(trainerUsername, firstName, lastName, isActive, trainingDate, durationInHours);

        return ResponseEntity.ok().build();
    }

    /**
     * Gets the training summary for a specific trainer.
     *
     * @param username the username of the trainer
     * @return a ResponseEntity containing the training summary
     */
    @GetMapping("/{username}/summary")
    public ResponseEntity<?> getTrainingSummary(@PathVariable String username) {
        return ResponseEntity.ok(trainerService.getTrainingSummary(username));
    }
}
