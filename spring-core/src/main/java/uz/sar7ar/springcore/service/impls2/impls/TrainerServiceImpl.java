package uz.sar7ar.springcore.service.impls2.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.model.entities.*;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TrainersTrainingsDto;
import uz.sar7ar.springcore.repository.jpa.*;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.TrainerService;
import uz.sar7ar.springcore.service.impls2.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainerServiceImpl implements TrainerService {
    private final UserService userService;
    private final TraineeService traineeService;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainerDto> getTrainerByUserName(String userName) {
        Optional<Trainer> optTrainer = trainerRepository.findTrainerByUserName(userName);
        return optTrainer.map(Trainer::toTrainerDto);
    }

    @Override
    public TrainerDto createTrainer(TrainerDto newTrainerDto) {
        log.info("Creating new Trainer");
        Trainer newTrainer = new Trainer();
        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(
                newTrainerDto.getSpecialization().getTrainingTypeName()).get();
        newTrainer.setSpecialization(trainingType);
        User createdUser = userService.createUser(newTrainerDto.getUser());
        newTrainer.setUser(createdUser);
        Trainer createdTrainer = trainerRepository.save(newTrainer);

        User user = userRepository.findByUserName(newTrainer.getUser().getUserName()).get();
        String rawPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        log.info("New Trainer created and persisted");

        TrainerDto createdTrainerDto = createdTrainer.toTrainerDto();
        createdTrainerDto.getUser().setPassword(rawPassword);
        return createdTrainerDto;
    }

    @Override
    public TrainerDto updateTrainer(TrainerDto toBeUpdatedTrainerDto) {
        log.info("Updating Trainer");
        Trainer oldTrainer = trainerRepository.findById(toBeUpdatedTrainerDto.getId()).get();
        User updatedUser = userService.updateUser(toBeUpdatedTrainerDto.getUser());
        TrainingType specialization = trainingTypeRepository
                .findByTrainingTypeName(toBeUpdatedTrainerDto.getSpecialization().getTrainingTypeName()).get();
        oldTrainer.setUser(updatedUser);
        oldTrainer.setSpecialization(specialization);
        Trainer updatedTrainer = trainerRepository.save(oldTrainer);
        log.info("Trainer updated and persisted");

        return updatedTrainer.toTrainerDto();
    }

    @Override
    public void deleteTrainerByUserName(String userName) {
        log.warn("Trainer profile deletion requested");
        trainerRepository.deleteTrainerByUserName(userName);
        log.warn("Trainer profile deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TrainerDto> getTrainersWhoseNotAssignedToTrainee(String traineeUserName)
                                                         throws UserNameNotFoundException {
        log.info("getTrainersWhoseNotAssignedToTrainee called");
        if(traineeService.getTraineeByUserName(traineeUserName).isEmpty()) throw new UserNameNotFoundException("Trainee not found");
        Set<Trainer> trainers = trainerRepository.TrainersWhoseNotAssignedToTrainee(traineeUserName);

        return trainers.stream().map(Trainer::toTrainerDto).collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TraineeDto> getTrainersTrainees(String trainerUserName) {
        return trainerRepository.getTrainersTrainees(trainerUserName)
                                .stream()
                                .map(Trainee::toTraineeDto)
                                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainersTrainingsDto> getTrainingsByCriteria(String trainerUserName,
                                                             String traineeUserName,
                                                             LocalDate fromDate,
                                                             LocalDate toDate) {
        List<Training> trainings = trainingRepository.findTrainersAllTrainings(trainerUserName);
        if (traineeUserName != null
                && !traineeUserName.isEmpty()
                && !traineeUserName.isBlank())
            trainings = trainings
                            .stream()
                            .filter(t -> t.getTrainee()
                                    .getUser()
                                    .getUserName()
                                    .equals(traineeUserName))
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
                            .filter(t -> t.getTrainingDate()
                                    .isBefore(toDate))
                            .toList();
        List<TrainersTrainingsDto> trainingToReturn = new ArrayList<>();
        trainings.forEach(t -> trainingToReturn
                                    .add(new TrainersTrainingsDto(
                                                t.getTrainingName(),
                                                t.getTrainee().getUser().getUserName(),
                                                t.getTrainingDate(),
                                                t.getTrainingType().getTrainingTypeName(),
                                                t.getDuration())));

        return trainingToReturn;
    }
}
