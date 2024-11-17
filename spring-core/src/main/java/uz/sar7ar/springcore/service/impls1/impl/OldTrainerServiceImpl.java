package uz.sar7ar.springcore.service.impls1.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.sar7ar.springcore.model.pojo_models.Trainer;
import uz.sar7ar.springcore.repository.pojo_dao.TrainerDao;
import uz.sar7ar.springcore.service.impls1.TrainerService;
import uz.sar7ar.springcore.utils.Utils;

@Service
@AllArgsConstructor
@Slf4j
public class OldTrainerServiceImpl implements TrainerService {
    private final TrainerDao trainerDao;

    @Override
    public void createTrainer(Trainer trainer) {
        log.info("createTrainer method of {} class was called", OldTrainerServiceImpl.class);
        trainer.setUsername(Utils.generateUserName(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(Utils.generateRandomPassword());
        trainerDao.create(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        log.info("updateTrainer method of {} class was called", OldTrainerServiceImpl.class);
        trainer.setUsername(Utils.generateUserName(trainer.getFirstName(), trainer.getLastName()));
        trainerDao.update(trainer);
    }

    @Override
    public Trainer selectTrainer(int trainerId) {
        log.info("selectTrainer method of {} class was called", OldTrainerServiceImpl.class);
        return trainerDao.getOne(trainerId);
    }
}
