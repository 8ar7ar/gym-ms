package uz.sar7ar.springcore.service.impls1.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.sar7ar.springcore.model.pojo_models.Trainee;
import uz.sar7ar.springcore.model.pojo_models.Trainer;
import uz.sar7ar.springcore.model.pojo_models.Training;
import uz.sar7ar.springcore.service.impls1.FacadeService;
import uz.sar7ar.springcore.service.impls1.TraineeService;
import uz.sar7ar.springcore.service.impls1.TrainerService;
import uz.sar7ar.springcore.service.impls1.TrainingService;

@Service
@AllArgsConstructor
public class OldFacadeServiceImpl implements FacadeService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Override
    public void createTrainee(Trainee trainee) {
        traineeService.createTrainee(trainee);
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        traineeService.updateTrainee(trainee);
    }

    @Override
    public void deleteTrainee(int traineeId) {
        traineeService.deleteTrainee(traineeId);
    }

    @Override
    public Trainee getTrainee(int traineeId) {
        return traineeService.selectTrainee(traineeId);
    }

    @Override
    public void createTrainer(Trainer trainer) {
        trainerService.createTrainer(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        trainerService.updateTrainer(trainer);
    }

    @Override
    public Trainer getTrainer(int trainerId) {
        return trainerService.selectTrainer(trainerId);
    }

    @Override
    public void createTraining(Training training) {
        trainingService.createTraining(training);
    }

    @Override
    public Training getTraining(String trainingName) {
        return trainingService.selectTraining(trainingName);
    }
}
