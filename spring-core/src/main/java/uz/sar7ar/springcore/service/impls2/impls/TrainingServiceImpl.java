package uz.sar7ar.springcore.service.impls2.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.*;
import uz.sar7ar.springcore.model.entities.dto.TrainingDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.mscomunication.TrainerWorkloadMicroService;
import uz.sar7ar.springcore.repository.jpa.TraineeRepository;
import uz.sar7ar.springcore.repository.jpa.TrainerRepository;
import uz.sar7ar.springcore.repository.jpa.TrainingRepository;
import uz.sar7ar.springcore.repository.jpa.TrainingTypeRepository;
import uz.sar7ar.springcore.service.impls2.TrainingService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainingServiceImpl implements TrainingService {
    private static final String DESTINATION = "trainingQueue";
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerWorkloadMicroService trainingWorkloadMicroService;
    private final JmsTemplate jmsTemplate;

    @Override
    public TrainingDto addTraining(TrainingDto newTrainingDto) {
        log.info("Creating training");
        Trainer trainer = trainerRepository.findById(newTrainingDto.getTrainer().getId()).get();
        Trainee trainee = traineeRepository.findById(newTrainingDto.getTrainee().getId()).get();
        TrainingType trainingType = trainingTypeRepository.findById(newTrainingDto.getTrainingType().getId()).get();
        Training newTraining = new Training();
        newTraining.setTrainee(trainee);
        newTraining.setTrainer(trainer);
        newTraining.setTrainingName(newTrainingDto.getTrainingName());
        newTraining.setTrainingType(trainingType);
        newTraining.setTrainingDate(newTrainingDto.getTrainingDate());
        newTraining.setDuration(newTrainingDto.getDuration());
        Training createdTraining = trainingRepository.save(newTraining);
        log.info("Training created and persisted");
        addTrainingSessionToTrainerInTrainerWorkloadMicroservice(createdTraining);
        convertTrainingToMessageAndSend(createdTraining);
        return createdTraining.toTrainingDto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTraineesAllTrainings(String traineeUserName) {
        return trainingRepository
                .findTraineesAllTrainings(traineeUserName)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTraineesTrainingsAfter(String traineeUserName,
                                                       LocalDate after) {
        return trainingRepository
                .findTraineesTrainingsAfter(traineeUserName, after)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTraineesTrainingsBefore(String traineeUserName,
                                                        LocalDate before) {
        return trainingRepository
                .findTraineesTrainingsBefore(traineeUserName, before)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTraineesTrainingsByTrainerUserName(String traineeUserName,
                                                                   String trainerUserName) {
        return trainingRepository
                .findTraineesTrainingsByTrainerUserName(traineeUserName, trainerUserName)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTraineesTrainingsByTrainingType(String traineeUserName,
                                                                TrainingTypeDto trainingTypeDto) {
        return trainingRepository
                .findTraineesTrainingsByTrainingType(traineeUserName,
                        trainingTypeRepository.findById(trainingTypeDto.getId()).get())
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainersAllTrainings(String trainerUserName) {
        return trainingRepository
                .findTrainersAllTrainings(trainerUserName)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainersTrainingsAfter(String trainerUserName,
                                                       LocalDate after) {
        return trainingRepository
                .findTrainersTrainingsAfter(trainerUserName, after)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainersTrainingsBefore(String trainerUserName,
                                                        LocalDate before) {
        return trainingRepository
                .findTrainersTrainingsBefore(trainerUserName, before)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainersTrainingsByTraineeUserName(String trainerUserName,
                                                                   String traineeUserName) {
        return trainingRepository
                .findTrainersTrainingsByTraineeUserName(trainerUserName, traineeUserName)
                .stream()
                .map(Training::toTrainingDto)
                .toList();
    }


    private void addTrainingSessionToTrainerInTrainerWorkloadMicroservice(Training training){
        String trainerUserName = training.getTrainer().getUser().getUserName();
        String trainerFirstName = training.getTrainer().getUser().getFirstName();
        String trainerLastName = training.getTrainer().getUser().getLastName();
        boolean isActive = training.getTrainer().getUser().getIsActive();
        LocalDate trainingDate = training.getTrainingDate();
        int durationInHours = training.getDuration();
        String ActionType = "ADD";
        trainingWorkloadMicroService.updateTrainerWorkload(
                trainerUserName, trainerFirstName, trainerLastName, isActive, trainingDate, durationInHours, ActionType);

    }

    private void convertTrainingToMessageAndSend(Training training) {
//        TrainingDtoMsg msg = TrainingDtoMsg
//                            .builder()
//                                .trainerUsername(training.getTrainer().getUser().getUserName())
//                                .firstName(training.getTrainer().getUser().getFirstName())
//                                .lastName(training.getTrainer().getUser().getLastName())
//                                .isActive(training.getTrainer().getUser().getIsActive())
//                                .trainingDate(training.getTrainingDate())
//                                .durationInHours(training.getDuration())
//                                .actionType("ADD")
//                            .build();
        jmsTemplate.convertAndSend(DESTINATION, message -> {
            message.setStringProperty("userName", training.getTrainer().getUser().getUserName());
            message.setStringProperty("firstName", training.getTrainer().getUser().getFirstName());
            message.setStringProperty("lastName", training.getTrainer().getUser().getLastName());
            message.setBooleanProperty("isActive", training.getTrainer().getUser().getIsActive());
            message.setStringProperty("trainingDate", training.getTrainingDate().toString());
            message.setIntProperty("durationInHours", training.getDuration());
            message.setStringProperty("actionType", "ADD");
            return message;}
        );
    }
}
