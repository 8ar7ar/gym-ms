package uz.sar7ar.springcore.service.impls1;

import uz.sar7ar.springcore.model.pojo_models.Trainer;

public interface TrainerService {
    void createTrainer(Trainer trainee);

    void updateTrainer(Trainer trainee);

    Trainer selectTrainer(int trainerId);
}
