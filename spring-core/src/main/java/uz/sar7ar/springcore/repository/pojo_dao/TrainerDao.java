package uz.sar7ar.springcore.repository.pojo_dao;

import uz.sar7ar.springcore.model.pojo_models.Trainer;

import java.util.List;

public interface TrainerDao {
    void create(Trainer trainee);

    void update(Trainer trainee);

    void delete(int trainerId);

    Trainer getOne(int trainerId);

    List<Trainer> getAll();
}
