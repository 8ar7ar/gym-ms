package uz.sar7ar.springcore.service.impls1.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.sar7ar.springcore.model.pojo_models.Training;
import uz.sar7ar.springcore.repository.pojo_dao.TrainingDao;
import uz.sar7ar.springcore.service.impls1.TrainingService;

@Service
@AllArgsConstructor
@Slf4j
public class OldTrainingServiceImpl implements TrainingService {
    private final TrainingDao trainingDao;

    @Override
    public void createTraining(Training training) {
        log.info("createTraining method of {} class was called", OldTrainingServiceImpl.class);
        trainingDao.create(training);
    }

    @Override
    public Training selectTraining(String trainingName) {
        log.info("selectTraining method of {} class was called", OldTrainingServiceImpl.class);
        return trainingDao.getOne(trainingName);
    }
}
