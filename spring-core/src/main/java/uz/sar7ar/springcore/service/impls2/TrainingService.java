package uz.sar7ar.springcore.service.impls2;

import uz.sar7ar.springcore.model.entities.dto.TrainingDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    TrainingDto addTraining(TrainingDto newTrainingDto);

    List<TrainingDto> getTraineesAllTrainings(String traineeUserName);

    List<TrainingDto> getTraineesTrainingsAfter(String traineeUserName, LocalDate after);

    List<TrainingDto> getTraineesTrainingsBefore(String traineeUserName, LocalDate before);

    List<TrainingDto> getTraineesTrainingsByTrainerUserName(String traineeUserName, String trainerUserName);

    List<TrainingDto> getTraineesTrainingsByTrainingType(String traineeUserName, TrainingTypeDto trainingTypeDto);

    List<TrainingDto> getTrainersAllTrainings(String trainersUserName);

    List<TrainingDto> getTrainersTrainingsAfter(String trainerUserName, LocalDate after);

    List<TrainingDto> getTrainersTrainingsBefore(String trainerUserName, LocalDate before);

    List<TrainingDto> getTrainersTrainingsByTraineeUserName(String trainerUserName, String traineeUserName);
}
