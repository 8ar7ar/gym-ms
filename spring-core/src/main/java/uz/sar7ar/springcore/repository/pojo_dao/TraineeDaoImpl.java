package uz.sar7ar.springcore.repository.pojo_dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.pojo_models.Trainee;
import uz.sar7ar.springcore.storage.StorageTrainee;

import java.util.*;

@Repository
@Slf4j
@AllArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    private final StorageTrainee traineeStorage;

    @Override
    public void create(Trainee trainee) {
        log.info("create method of {} class was called", TraineeDaoImpl.class);

        traineeStorage.getTrainees().put(trainee.getTraineeId(), trainee);
    }

    @Override
    public void update(Trainee trainee) {
        log.info("update method of {} class was called", TraineeDaoImpl.class);
        traineeStorage.getTrainees().put(trainee.getTraineeId(), trainee);
    }

    @Override
    public void delete(int traineeId) {
        log.info("delete method of {} class was called", TraineeDaoImpl.class);
        traineeStorage.getTrainees().remove(traineeId);
    }

    @Override
    public Trainee getOne(int traineeId) {
        log.info("getOne method of {} class was called", TraineeDaoImpl.class);

        Trainee trainee = traineeStorage.getTrainees().get(traineeId);
        if (trainee == null) {
            log.warn("Trainee with given traineeId={} does not exist", traineeId);
            return null;
        }
        return trainee;
    }

    @Override
    public List<Trainee> getAll() {
        log.info("getAll method of {} class was called", TraineeDaoImpl.class);
        return traineeStorage.getTrainees().values().stream().toList();
    }
}
