package uz.sar7ar.springcore.service.impls2;

import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TrainersTrainingsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerService {
    Optional<TrainerDto> getTrainerByUserName(String userName);

    TrainerDto createTrainer(TrainerDto newTrainerDto);

    TrainerDto updateTrainer(TrainerDto toBeUpdatedTrainerDto);

    void deleteTrainerByUserName(String userName);

    Set<TrainerDto> getTrainersWhoseNotAssignedToTrainee(String traineeUserName) throws UserNameNotFoundException;

    Set<TraineeDto> getTrainersTrainees(String trainerUserName);

    List<TrainersTrainingsDto> getTrainingsByCriteria(String trainerUserName,
                                                      String traineeUserName,
                                                      LocalDate fromDate,
                                                      LocalDate toDate);
}
