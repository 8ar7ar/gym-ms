package uz.sar7ar.trainerworkload.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sar7ar.trainerworkload.service.TrainerService;
import uz.sar7ar.trainerworkload.service.TrainerSummaryService;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/trainer-workload-service")
public class TrainerController {
    private TrainerService trainerService;
    private TrainerSummaryService trainerSummaryService;

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

    @GetMapping("/{username}/summary")
    public ResponseEntity<?> getTrainingSummary(@PathVariable String username) {
        return ResponseEntity.ok(trainerService.getTrainingSummary(username));
    }
}
