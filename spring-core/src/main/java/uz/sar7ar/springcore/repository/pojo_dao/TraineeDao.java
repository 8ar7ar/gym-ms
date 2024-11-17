package uz.sar7ar.springcore.repository.pojo_dao;

import uz.sar7ar.springcore.model.pojo_models.Trainee;

import java.util.List;

public interface TraineeDao {
    void create(Trainee trainee);

    void update(Trainee trainee);

    void delete(int traineeId);

    Trainee getOne(int traineeId);

    List<Trainee> getAll();
}
