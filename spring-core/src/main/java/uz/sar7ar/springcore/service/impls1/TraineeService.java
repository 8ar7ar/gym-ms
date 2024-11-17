package uz.sar7ar.springcore.service.impls1;

import uz.sar7ar.springcore.model.pojo_models.Trainee;

public interface TraineeService {
    void createTrainee(Trainee trainee);

    void updateTrainee(Trainee trainee);

    void deleteTrainee(int traineeId);

    Trainee selectTrainee(int traineeId);
}
