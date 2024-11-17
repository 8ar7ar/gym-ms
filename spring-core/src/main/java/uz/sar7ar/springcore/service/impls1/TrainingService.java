package uz.sar7ar.springcore.service.impls1;

import uz.sar7ar.springcore.model.pojo_models.Training;

public interface TrainingService {
    void createTraining(Training training);

    Training selectTraining(String trainingName);
}
