package uz.sar7ar.springcore.repository.pojo_dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.pojo_models.Training;
import uz.sar7ar.springcore.storage.StorageTraining;

import java.util.List;

@Repository
@Slf4j
@AllArgsConstructor
public class TrainingDaoImpl implements TrainingDao {
    private final StorageTraining trainingStorage;
    private final StorageTraining storageTraining;

    @Override
    public void create(Training training) {
        log.info("create method of {} class was called", TrainingDaoImpl.class);
        trainingStorage.getTrainings().put(training.getTrainingName(), training);
    }

    @Override
    public void update(Training training) {
        log.info("update method of {} class was called", TrainingDaoImpl.class);
        create(training);
    }

    @Override
    public void delete(String trainingName) {
        log.info("delete method of {} class was called", TrainingDaoImpl.class);
        trainingStorage.getTrainings().remove(trainingName);
    }

    @Override
    public Training getOne(String trainingName) {
        log.info("getOne method of {} class was called", TrainingDaoImpl.class);

        Training training = storageTraining.getTrainings().get(trainingName);
        if (training == null) {
            log.warn("Training with given trainingName={} does not exist", trainingName);
            return null;
        }
        return training;
    }

    @Override
    public List<Training> getAll() {
        log.info("getAll method of {} class was called", TrainingDaoImpl.class);
        return trainingStorage.getTrainings().values().stream().toList();
    }
}
