package uz.sar7ar.springcore.service.impls2.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.*;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.UserDto;
import uz.sar7ar.springcore.model.entities.dto.TraineesTrainingsDto;
import uz.sar7ar.springcore.repository.jpa.TraineeRepository;
import uz.sar7ar.springcore.repository.jpa.TrainerRepository;
import uz.sar7ar.springcore.repository.jpa.TrainingRepository;
import uz.sar7ar.springcore.repository.jpa.UserRepository;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.UserService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TraineeServiceImpl implements TraineeService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    @Override
    public List<TraineeDto> getAllTrainees() {
        log.info("Retrieving all trainees as list...");

        return traineeRepository.findAll().stream().map(Trainee::toTraineeDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TraineeDto> getTraineeByUserName(String userName) {
        log.info("retrieving trainee with userName: [{}]", userName);
        Optional<Trainee> optTrainee = traineeRepository.findTraineeByUserName(userName);

        return optTrainee.map(Trainee::toTraineeDto);
    }

    @Override
    public TraineeDto createTrainee(TraineeDto newTraineeDto) {
        log.info("Creating new trainee");
        Trainee newTrainee = new Trainee();
        newTrainee.setDateOfBirth(newTraineeDto.getDateOfBirth());
        newTrainee.setAddress(newTraineeDto.getAddress());
        User createdUser = userService.createUser(newTraineeDto.getUser());
        newTrainee.setUser(createdUser);
        Trainee createdTrainee = traineeRepository.save(newTrainee);

        User user = userRepository.findByUserName(createdTrainee.getUser().getUserName()).get();
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
        log.info("New trainee created and persisted");

        TraineeDto traineeDto = createdTrainee.toTraineeDto();
        traineeDto.getUser().setPassword(rawPassword);
        return traineeDto;
    }

    @Override
    public TraineeDto updateTrainee(TraineeDto toBeUpdatedTraineeDto) {
        log.info("Updating trainee");
        UserDto oldUser = toBeUpdatedTraineeDto.getUser();
        User updatedUser = userService.updateUser(oldUser);
        Set<Trainer> traineesTrainers = new HashSet<>();
        Trainee oldTrainee = traineeRepository.findById(toBeUpdatedTraineeDto.getId()).get();
        oldTrainee.setDateOfBirth(toBeUpdatedTraineeDto.getDateOfBirth());
        oldTrainee.setAddress(toBeUpdatedTraineeDto.getAddress());
        oldTrainee.setUser(updatedUser);
        toBeUpdatedTraineeDto.getTrainers()
                             .stream()
                             .map(trainerDto -> trainerDto.getUser().getUserName())
                             .forEach(trainerUserName-> {
                                Optional<Trainer> optTrainer = trainerRepository.findTrainerByUserName(trainerUserName);
                                optTrainer.ifPresent(traineesTrainers::add);
                             });
        oldTrainee.setTrainers(traineesTrainers);

        traineesTrainers.forEach(trainer ->trainer.getTrainees().add(oldTrainee));
        trainerRepository.saveAll(traineesTrainers);

        Trainee updatedTrainee = traineeRepository.save(oldTrainee);
        log.info("Trainee updated and persisted");

        return updatedTrainee.toTraineeDto();
    }

    @Override
    public void deleteTraineeByUserName(String userName) {
        log.warn("Trainee profile deletion requested");
        traineeRepository.deleteTraineeByUserName(userName);
        trainerRepository.flush();
        log.warn("Trainee profile deleted");
    }

    @Override
    public void updateTraineeTrainersList(TraineeDto traineeDto, Set<TrainerDto> trainerDtoList) {
        log.info("Updating trainees trainers list");
        Trainee trainee = traineeRepository.findById(traineeDto.getId()).get();
        List<Trainer> trainers = trainerRepository.findAllById(trainerDtoList
                                                                .stream()
                                                                .map(TrainerDto::getId)
                                                                .toList());
        trainee.setTrainers(new HashSet<>(trainers));
        trainers.forEach(trainer-> trainer.getTrainees().add(trainee));
        trainerRepository.saveAll(trainers);
        traineeRepository.saveAndFlush(trainee);
        log.info("Trainees training list updated");
    }

    @Override
    public void assignTrainerToTrainee(TraineeDto traineeDto, TrainerDto trainerDto) {
        log.info("Assigning trainerId to trainee");
        Trainee trainee = traineeRepository.findById(traineeDto.getId()).get();
        Trainer trainer = trainerRepository.findById(trainerDto.getId()).get();
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
        traineeRepository.saveAndFlush(trainee);
        trainerRepository.saveAndFlush(trainer);
        log.info("Trainer assigned to trainee successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineesTrainingsDto> getTrainingsByCriteria(String traineeUserName,
                                                             String trainerUserName,
                                                             TrainingTypeEnum trainingType,
                                                             LocalDate fromDate,
                                                             LocalDate toDate) {
        List<Training> trainings = trainingRepository.findTraineesAllTrainings(traineeUserName);
        if (trainerUserName != null
                && !trainerUserName.isEmpty()
                && !trainerUserName.isBlank())
            trainings = trainings
                            .stream()
                            .filter(t -> t.getTrainer()
                                                  .getUser()
                                                  .getUserName()
                                                  .equals(trainerUserName))
                            .toList();
        if (trainingType != null)
            trainings = trainings
                            .stream()
                            .filter(t -> t.getTrainingType()
                                                  .getTrainingTypeName()
                                                  .equals(trainingType))
                            .toList();
        if (fromDate != null)
            trainings = trainings
                            .stream()
                            .filter(t -> t.getTrainingDate()
                                                    .isAfter(fromDate.minusDays(1)))
                            .toList();
        if (toDate != null)
            trainings = trainings
                    .stream()
                    .filter(t -> t.getTrainingDate().isBefore(toDate))
                    .toList();
        List<TraineesTrainingsDto> trainingToReturn = new ArrayList<>();
        trainings.forEach(t -> trainingToReturn
                                    .add(new TraineesTrainingsDto(
                                            t.getTrainingName(),
                                            t.getTrainer().getUser().getUserName(),
                                            t.getTrainingDate(),
                                            t.getTrainingType().getTrainingTypeName(),
                                            t.getDuration())));

        return trainingToReturn;
    }
}
