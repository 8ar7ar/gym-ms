package uz.sar7ar.springcore.service.impls1.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.sar7ar.springcore.model.pojo_models.Trainee;
import uz.sar7ar.springcore.repository.pojo_dao.TraineeDao;
import uz.sar7ar.springcore.service.impls1.TraineeService;
import uz.sar7ar.springcore.utils.Utils;

@Service
@AllArgsConstructor
@Slf4j
public class OldTraineeServiceImpl implements TraineeService {
    private final TraineeDao traineeDao;

    @Override
    public void createTrainee(Trainee trainee) {
        log.info("createTrainee method of {} class was called", OldTraineeServiceImpl.class);
        trainee.setUsername(Utils.generateUserName(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(Utils.generateRandomPassword());
        traineeDao.create(trainee);
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        log.info("updateTrainer method of {} class was called", OldTraineeServiceImpl.class);
        trainee.setUsername(Utils.generateUserName(trainee.getFirstName(), trainee.getLastName()));
        traineeDao.update(trainee);
    }

    @Override
    public void deleteTrainee(int traineeId) {
        log.info("deleteTraineeByUserName method of {} class was called", OldTraineeServiceImpl.class);
        traineeDao.delete(traineeId);
    }

    @Override
    public Trainee selectTrainee(int traineeId) {
        log.info("selectTrainee method of {} class was called", OldTraineeServiceImpl.class);
        return traineeDao.getOne(traineeId);
    }
}
