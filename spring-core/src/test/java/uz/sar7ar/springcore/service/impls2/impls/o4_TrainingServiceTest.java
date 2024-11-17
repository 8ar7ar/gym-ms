package uz.sar7ar.springcore.service.impls2.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.*;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.TrainerService;
import uz.sar7ar.springcore.service.impls2.TrainingService;
import uz.sar7ar.springcore.service.impls2.TrainingTypeService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class o4_TrainingServiceTest {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;


    @Autowired
    public o4_TrainingServiceTest(TrainingService trainingService,
                                  TraineeService traineeService,
                                  TrainerService trainerService,
                                  TrainingTypeService trainingTypeService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Test
    void addTraining() {
        TraineeDto trainee = traineeService.getTraineeByUserName("Brit.Francis").get(); // userId = 2
        TrainerDto trainer = trainerService.getTrainerByUserName("Erroll.Oldman").get(); // userId = 97, trid= 17
        String trainingName = "Test Training";
        TrainingTypeDto trainingType = trainingTypeService.findTrainingTypeById(2).get();
        LocalDate trainingStartDate = LocalDate.of(2024, 12, 31);
        Integer duration = 90;

        TrainingDto TrainingDto = new TrainingDto(null, trainee, trainer, trainingName,
                                                     trainingType, trainingStartDate, duration);

        TrainingDto createdTraining = trainingService.addTraining(TrainingDto);

        assertEquals(trainee.getId(), createdTraining.getTrainee().getId());
        assertEquals(trainer.getId(), createdTraining.getTrainer().getId());
        assertEquals(trainingName, createdTraining.getTrainingName());
        assertEquals(trainingName, createdTraining.getTrainingName());
        assertEquals(trainingType.getTrainingTypeName(), createdTraining.getTrainingType().getTrainingTypeName());
        assertEquals(trainingStartDate, createdTraining.getTrainingDate());
        assertEquals(duration, createdTraining.getDuration());
    }

    @Test
    void getTraineesTrainingsAfter() {
        List<TrainingDto> trainings = trainingService.getTraineesTrainingsAfter(
                "Findley.Loutheane",LocalDate.of(2025, 3, 1));
        assertEquals(3, trainings.size());
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Galearis")));
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Calammophila")));
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Fishtail Swordfern")));
    }

    @Test
    void getTraineesTrainingsBefore() {
        List<TrainingDto> trainings = trainingService
                .getTraineesTrainingsAfter("Findley.Loutheane", LocalDate.of(2025, 3, 29));
        assertEquals(2, trainings.size());
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Calammophila")));
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Fishtail Swordfern")));
    }

    @Test
    void getTraineesTrainingsByTrainerUserName() {
//        ('Erroll', 'Oldman', 'Erroll.Oldman', 'er89yAf657', true); trainer id = 17
        List<TrainingDto> trainings = trainingService
                .getTraineesTrainingsByTrainerUserName("Findley.Loutheane","Erroll.Oldman");
        assertEquals(1, trainings.size());
        assertTrue(trainings.stream()
                .map(trainingDto -> trainingDto.getTrainer().getUser().getUserName())
                .allMatch((name)->name.equals("Erroll.Oldman")));
    }

    @Test
    void getTraineesTrainingsByTrainingType() {
        TrainingTypeDto trainingTypeDto = trainingTypeService.findTrainingTypeById(1).get();

        List<TrainingDto> trainings = trainingService
                .getTraineesTrainingsByTrainingType("Findley.Loutheane", trainingTypeDto);
        assertEquals(3, trainings.size());
        assertTrue(trainings
                .stream()
                .map(trainingDto -> trainingDto
                                    .getTrainingType()
                                    .getTrainingTypeName())
                .allMatch(trainingTypeEnum -> trainingTypeEnum.equals(TrainingTypeEnum.RESISTANCE)));
    }

    @Test
    void getTrainersTrainingsAfter() {
//        (39, 10, 'Roadside Toadflax', 1, '2025-05-10', 40);
//        (37, 10, 'Cartilage Lichen', 3, '2025-07-13', 89);
//        (3, 10, 'Canadian Wildrye', 4, '2025-07-06', 28);
//        (3, 10, 'Tall Buckwheat', 1, '2025-06-03', 34);
//        (62, 10, 'Cojoba', 2, '2024-09-17', 12);
//        ('Jude', 'Spinley', 'Jude.Spinley', '5M40oid852', true); userid = 90; trainer
//        ('Hester', 'Maillard', 'Hester.Maillard', 'Va47rKo102', true); user id = 3 trainee
        List<TrainingDto> trainings =  trainingService
                .getTrainersTrainingsAfter("Jude.Spinley", LocalDate.of(2025, 1, 1));
        assertEquals(4, trainings.size());
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Roadside Toadflax")));
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Cartilage Lichen")));
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Canadian Wildrye")));
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Tall Buckwheat")));
    }

    @Test
    void getTrainersTrainingsBefore() {
        List<TrainingDto> trainings =  trainingService
                .getTrainersTrainingsBefore("Jude.Spinley", LocalDate.of(2025, 1, 1));
        assertEquals(1, trainings.size());
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Cojoba")));
    }

    @Test
    void getTrainersTrainingsByTraineeUserName() {
        var trainings = trainingService
                .getTrainersTrainingsByTraineeUserName("Jude.Spinley","Hester.Maillard");
        assertEquals(1, trainings.size());
        assertTrue(trainings.stream().map(TrainingDto::getTrainingName).anyMatch((name)->name.equals("Canadian Wildrye")));
    }
}