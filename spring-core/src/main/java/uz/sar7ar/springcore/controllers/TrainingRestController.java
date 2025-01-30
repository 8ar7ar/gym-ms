package uz.sar7ar.springcore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.TrainerService;
import uz.sar7ar.springcore.service.impls2.TrainingService;
import uz.sar7ar.springcore.service.impls2.TrainingTypeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainings")
public class TrainingRestController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @PostMapping
    @Operation(summary = "Add Training")
    public ResponseEntity<String> createTraining(@RequestParam("trainee-username") String traineeUserName,
                                                 @RequestParam("trainer-username") String trainerUserName,
                                                 @RequestParam("training-name") String trainingName,
                                                 @RequestParam("training-type") TrainingTypeEnum trainingType,
                                                 @Parameter(description = "format: yyyy-mm-dd")
                                                 @RequestParam("training-date") LocalDate trainingDate,
                                                 @Parameter(description = "number of days")
                                                 @RequestParam("training-duration") Integer duration)
            throws UserNameNotFoundException {
        Optional<TraineeDto> optTraineeDto = traineeService.getTraineeByUserName(traineeUserName);
        Optional<TrainerDto> optTrainerDto = trainerService.getTrainerByUserName(trainerUserName);
        Optional<TrainingTypeDto> optTrainingTypeDto = trainingTypeService.findTrainingTypeByName(trainingType);

        TraineeDto traineeDto;
        TrainerDto trainerDto;
        TrainingTypeDto trainingTypeDto;

        if (optTraineeDto.isPresent()) traineeDto = optTraineeDto.get();
        else throw new UserNameNotFoundException("Trainee << " + traineeUserName + " >> not found");
        if (optTrainerDto.isPresent()) trainerDto = optTrainerDto.get();
        else throw new UserNameNotFoundException("Trainer << " + trainerUserName + " >> not found");
        if (optTrainingTypeDto.isPresent()) trainingTypeDto = optTrainingTypeDto.get();
        else throw new UserNameNotFoundException("Training << " + trainerUserName + " >> not found");

        TrainingDto newTrainingDto = new TrainingDto(traineeDto, trainerDto, trainingName,
                trainingTypeDto, trainingDate, duration);
        TrainingDto createdTrainingDto = trainingService.addTraining(newTrainingDto);


        return ResponseEntity
                .status(HttpStatus.OK).
                body("<< " + createdTrainingDto.getTrainingName() + " >> training successfully created.");
    }

    @GetMapping("/training-types")
    @Operation(summary = "Get Training types")
    public ResponseEntity<List<TrainingTypeDto>> getAllTrainingTypes() {
        List<TrainingTypeDto> trainingTypes = trainingTypeService.findAllTrainingTypes();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainingTypes);
    }
}
