package uz.sar7ar.springcore.repository.pojo_dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.pojo_models.Trainer;
import uz.sar7ar.springcore.storage.StorageTrainer;

import java.util.List;

@Repository
@Slf4j
@AllArgsConstructor
public class TrainerDaoImpl implements TrainerDao {
    private final StorageTrainer storageTrainer;

    @Override
    public void create(Trainer trainer) {
        log.info("create method of {} class was called", TrainerDaoImpl.class);
        storageTrainer.getTrainers().put(trainer.getTrainerId(), trainer);
    }

    @Override
    public void update(Trainer trainer) {
        log.info("update method of {} class was called", TrainerDaoImpl.class);
        storageTrainer.getTrainers().put(trainer.getTrainerId(), trainer);
    }

    @Override
    public void delete(int trainerId) {
        log.info("delete method of {} class was called", TrainerDaoImpl.class);
        storageTrainer.getTrainers().remove(trainerId);
    }

    @Override
    public Trainer getOne(int trainerId) {
        log.info("getOne method of {} class was called", TrainerDaoImpl.class);

        Trainer trainer = storageTrainer.getTrainers().get(trainerId);
        if (trainer == null) {
            log.warn("Trainer with given trainerId={} does not exist", trainerId);
            return null;
        }
        return trainer;
    }

    @Override
    public List<Trainer> getAll() {
        log.info("getAll method of {} class was called", TrainerDaoImpl.class);
        return storageTrainer.getTrainers().values().stream().toList();
    }
}
