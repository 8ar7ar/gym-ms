package uz.sar7ar.springcore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sar7ar.springcore.exceptions.InvalidUserPasswordException;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TraineesTrainingsDto;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.TrainerService;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainees")
public class TraineeRestController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @GetMapping("/{username}")
    @Operation(summary = "Get Trainee Profile")
    public ResponseEntity<TraineeDto> getTraineeByUserName(@PathVariable("username") String userName)
                                                    throws UserNameNotFoundException {
        Optional<TraineeDto> trainee = traineeService.getTraineeByUserName(userName);
        TraineeDto dtoToReturn;
        if (trainee.isEmpty()) throw new UserNameNotFoundException("Trainee << " + userName +
                                                                   " >> not found. Check your spelling please.");
        else {
            dtoToReturn = trainee.get();
            dtoToReturn.getUser().setPassword(null);
            dtoToReturn.getTrainers().forEach(trainerDto -> trainerDto.getUser().setPassword(null));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoToReturn);
    }

    @GetMapping()
    @Operation(summary = "Returns all trainees as List")
    public ResponseEntity<List<TraineeDto>> getAllTrainees(){
        List<TraineeDto> trainees = traineeService.getAllTrainees();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainees);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update Trainee Profile")
    public ResponseEntity<TraineeDto> updateTraineeProfile(@PathVariable("username") String userName,
                                                           @RequestParam("password") String password,
                                                           @RequestParam("first-name") String firstName,
                                                           @RequestParam("last-name") String lastName,
                                                           @Parameter(description = "format: yyyy-mm-dd")
                                                           @RequestParam(value = "date-of-birth", required = false) LocalDate dateOfBirth,
                                                           @RequestParam(value = "address", required = false) String address,
                                                           @RequestParam("is-active") Boolean isActive)
                                                    throws UserNameNotFoundException,
                                                           InvalidUserPasswordException {


        Optional<TraineeDto> optTraineeDto = traineeService.getTraineeByUserName(userName);
        TraineeDto oldTraineeDto;
        if (optTraineeDto.isPresent()) oldTraineeDto = optTraineeDto.get();
        else throw new UserNameNotFoundException("Trainee << " + userName + " >> not found. Are you sure that you are trainee?");

        oldTraineeDto.getUser().setFirstName(firstName);
        oldTraineeDto.getUser().setLastName(lastName);
        oldTraineeDto.getUser().setIsActive(isActive);
        if(dateOfBirth != null) oldTraineeDto.setDateOfBirth(dateOfBirth);
        if(address != null) oldTraineeDto.setAddress(address);

        TraineeDto updatedTraineeDto = traineeService.updateTrainee(oldTraineeDto);
        updatedTraineeDto.getUser().setPassword(null);
        updatedTraineeDto.getTrainers().forEach(trainer -> {trainer.getUser().setPassword(null);
                                                                trainer.getUser().setIsActive(null);});

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTraineeDto);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete Trainee Profile")
    public ResponseEntity<String> deleteTraineeProfile(@PathVariable("username") String username,
                                                       @RequestParam() String password){
        traineeService.deleteTraineeByUserName(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Trainee profile << " + username + " >> was deleted.");
    }

    @GetMapping("/{username}/not-assigned-trainers")
    @Operation(summary = "Get not assigned on trainee active trainers")
    public ResponseEntity<Set<TrainerDto>> getNotAssignedTrainersToTrainee(@PathVariable("username") String userName)
                                                                    throws UserNameNotFoundException {
        Set<TrainerDto> trainers = trainerService.getTrainersWhoseNotAssignedToTrainee(userName);
        trainers.forEach(trainerDto -> trainerDto.getUser().setPassword(null));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainers);
    }

    @PutMapping("/{username}/update-trainers")
    @Operation(summary = "Update Trainee's Trainer List")
    public ResponseEntity<Set<TrainerDto>> updateTraineesTrainers(@PathVariable("username") String traineeUserName,
                                                                  @RequestBody List<String> trainersUserNames)
                                                           throws UserNameNotFoundException{
        Set<TrainerDto> trainersToAssignToTrainee = new HashSet<>();
        trainersUserNames.forEach(trainerUserName -> {
                                    Optional<TrainerDto> trainer = trainerService.getTrainerByUserName(trainerUserName);
                                    trainer.ifPresent(trainersToAssignToTrainee::add);});
        TraineeDto traineeDto;
        Optional<TraineeDto> optTraineeDto = traineeService.getTraineeByUserName(traineeUserName);
        if (optTraineeDto.isPresent()) traineeDto = optTraineeDto.get();
        else throw new UserNameNotFoundException("Trainee << " + traineeUserName + " >> not found. Are you sure that you are trainee?");
        traineeDto.setTrainers(trainersToAssignToTrainee);

        traineeService.updateTraineeTrainersList(traineeDto, trainersToAssignToTrainee);


        Set<TrainerDto> updatedTrainersToReturn = traineeService.getTraineeByUserName(traineeUserName).get().getTrainers();
        updatedTrainersToReturn.forEach(trainerDto -> {trainerDto.getUser().setIsActive(null);
                                                       trainerDto.getUser().setPassword(null);
                                                       trainerDto.setTrainees(null);});

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTrainersToReturn);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainee Trainings List")
    public ResponseEntity<List<TraineesTrainingsDto>> getTraineesTrainingsByCriteria(
                                                        @PathVariable("username") String traineeUserName,
                                                        @RequestParam(required = false) String trainerUserName,
                                                        @RequestParam(required = false) TrainingTypeEnum trainingType,
                                                        @Parameter(description = "format: yyyy-mm-dd")
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                        @Parameter(description = "format: yyyy-mm-dd")
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate)
                                                 throws UserNameNotFoundException {
        if (traineeService.getTraineeByUserName(traineeUserName).isEmpty())
            throw new UserNameNotFoundException("Trainee << " + traineeUserName + " >> not found. Check your spelling please.");
        List<TraineesTrainingsDto> trainings = traineeService.getTrainingsByCriteria(traineeUserName,
                                                                                     trainerUserName,
                                                                                     trainingType,
                                                                                     fromDate,
                                                                                     toDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainings);
    }

    @PatchMapping("/{username}")
    @Operation(summary = "Activate/De-Activate Trainee")
    public ResponseEntity<String> activateDeActivateTrainee(@PathVariable("username") String userName,
                                                            @RequestParam(value = "is-active") Boolean isActive)
                                                     throws UserNameNotFoundException {
        Optional<TraineeDto> optTraineeDto = traineeService.getTraineeByUserName(userName);
        TraineeDto traineeDto;
        if (optTraineeDto.isPresent())  traineeDto = optTraineeDto.get();
        else throw new UserNameNotFoundException("Trainee << " + userName + " >> not found. Check your spelling please.");

        traineeDto.getUser().setIsActive(isActive);
        traineeService.updateTrainee(traineeDto);
        String msg = (isActive)
                ? "Trainee << " + userName + " >> is activated."
                : "Trainee << " + userName + " >> is de-activated.";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(msg);
    }
}
