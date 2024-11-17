package uz.sar7ar.springcore.service.impls1;

import uz.sar7ar.springcore.model.pojo_models.Trainee;
import uz.sar7ar.springcore.model.pojo_models.Trainer;
import uz.sar7ar.springcore.model.pojo_models.Training;

public interface FacadeService {
    void createTrainee(Trainee trainee);

    void updateTrainee(Trainee trainee);

    void deleteTrainee(int traineeId);

    Trainee getTrainee(int traineeId);

    void createTrainer(Trainer trainer);

    void updateTrainer(Trainer trainer);

    Trainer getTrainer(int trainerId);

    void createTraining(Training training);

    Training getTraining(String trainingName);

}
