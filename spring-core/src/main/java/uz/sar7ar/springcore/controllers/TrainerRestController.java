package uz.sar7ar.springcore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainersTrainingsDto;
import uz.sar7ar.springcore.service.impls2.TrainerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerRestController {
    private final TrainerService trainerService;

    @GetMapping("/{username}")
    @Operation(summary = "Get Trainer Profile")
    public ResponseEntity<TrainerDto> getTrainerByUserName(@PathVariable("username") String userName)
                                                    throws UserNameNotFoundException {
        TrainerDto dtoToReturn;
        Optional<TrainerDto> trainer = trainerService.getTrainerByUserName(userName);
        if (trainer.isPresent()) dtoToReturn = trainer.get();
        else throw new UserNameNotFoundException("Trainer << " + userName + " >> not found. Check your spelling please.");

        Set<TraineeDto> trainees = trainerService.getTrainersTrainees(userName);
        trainees.forEach(trainee -> {trainee.setTrainers(null);
                                     trainee.setDateOfBirth(null);
                                     trainee.setAddress(null);
                                     trainee.getUser().setPassword(null);
                                     trainee.getUser().setIsActive(null);});
        dtoToReturn.setTrainees(trainees);
        dtoToReturn.getUser().setPassword(null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoToReturn);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update Trainer Profile")
    public ResponseEntity<TrainerDto> updateTrainerProfile(@PathVariable("username") String userName,
                                                           @RequestParam("password") String password,
                                                           @RequestParam("first-name") String firstName,
                                                           @RequestParam("last-name") String lastName,
                                                           @RequestParam("specialization") TrainingTypeEnum specialization,
                                                           @RequestParam("is-active") Boolean isActive)
                                                    throws UserNameNotFoundException{
        Optional<TrainerDto> optTrainerDto = trainerService.getTrainerByUserName(userName);
        TrainerDto trainerDto;
        if (optTrainerDto.isPresent()) trainerDto = optTrainerDto.get();
        else throw new UserNameNotFoundException("Trainer << " + userName + " >> not found. Are you sure that you are trainer?");

        trainerDto.getUser().setFirstName(firstName);
        trainerDto.getUser().setLastName(lastName);
        trainerDto.getUser().setIsActive(isActive);
        trainerDto.setSpecialization(new TrainingTypeDto(specialization));

        TrainerDto updatedTrainer = trainerService.updateTrainer(trainerDto);
        Set<TraineeDto> trainees = trainerService.getTrainersTrainees(updatedTrainer.getUser().getUserName());
        trainees.forEach(trainee -> {trainee.setTrainers(null);
                                     trainee.getUser().setPassword(null);
                                     trainee.setDateOfBirth(null);
                                     trainee.setAddress(null);
                                     trainee.getUser().setIsActive(null);});
        updatedTrainer.getUser().setPassword(null);
        updatedTrainer.setTrainees(trainees);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTrainer);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainer Trainings List")
    public ResponseEntity<List<TrainersTrainingsDto>> getTrainersTrainingsByCriteria(
                                                        @PathVariable("username") String trainerUserName,
                                                        @RequestParam(required = false) String traineeUserName,
                                                        @Parameter(description = "format: yyyy-mm-dd")
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                        @Parameter(description = "format: yyyy-mm-dd")
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate)
                                                 throws UserNameNotFoundException {
        if (trainerService.getTrainerByUserName(trainerUserName).isEmpty())
            throw new UserNameNotFoundException("Trainer << " + trainerUserName + " >> not found. Check your spelling please.");
        List<TrainersTrainingsDto> trainings = trainerService.getTrainingsByCriteria(trainerUserName,
                                                                                     traineeUserName,
                                                                                     fromDate,
                                                                                     toDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainings);
    }

    @PatchMapping("/{username}")
    @Operation(summary = "Activate/De-Activate Trainer")
    public ResponseEntity<String> activateDeActivateTrainer(@PathVariable("username") String userName,
                                                            @RequestParam("is-active") Boolean isActive)
                                                     throws UserNameNotFoundException {
        Optional<TrainerDto> optTrainerDto = trainerService.getTrainerByUserName(userName);
        TrainerDto trainerDto;
        if (optTrainerDto.isPresent())  trainerDto = optTrainerDto.get();
        else throw new UserNameNotFoundException("Trainer << " + userName + " >> not found. Check your spelling please.");
        trainerDto.getUser().setIsActive(isActive);
        trainerService.updateTrainer(trainerDto);
        String msg = (isActive)
                ? "Trainer << " + userName + " >> is activated."
                : "Trainer << " + userName + " >> is de-activated.";

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(msg);
    }
}
