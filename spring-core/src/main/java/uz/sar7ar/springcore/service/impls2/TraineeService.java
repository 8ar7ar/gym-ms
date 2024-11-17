package uz.sar7ar.springcore.service.impls2;

import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TraineesTrainingsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TraineeService {
    List<TraineeDto> getAllTrainees();

    Optional<TraineeDto> getTraineeByUserName(String userName);

    TraineeDto createTrainee(TraineeDto newTraineeDto);

    TraineeDto updateTrainee(TraineeDto toBeUpdatedTraineeDto);

    void deleteTraineeByUserName(String userName);

    void updateTraineeTrainersList(TraineeDto traineeDto, Set<TrainerDto> trainerDtoList);

    void assignTrainerToTrainee(TraineeDto traineeDto, TrainerDto trainerDto);

    List<TraineesTrainingsDto> getTrainingsByCriteria(String traineeUserName,
                                                      String trainerUserName,
                                                      TrainingTypeEnum trainingType,
                                                      LocalDate fromDate,
                                                      LocalDate toDate);
}
