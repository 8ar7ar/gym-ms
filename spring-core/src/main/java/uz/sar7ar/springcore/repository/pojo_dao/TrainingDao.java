package uz.sar7ar.springcore.repository.pojo_dao;

import uz.sar7ar.springcore.model.pojo_models.Training;

import java.util.List;

public interface TrainingDao {
    void create(Training training);

    void update(Training training);

    void delete(String trainingName);

    Training getOne(String trainingName);

    List<Training> getAll();
}
