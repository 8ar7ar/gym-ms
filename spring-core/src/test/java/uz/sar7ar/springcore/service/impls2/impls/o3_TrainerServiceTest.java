package uz.sar7ar.springcore.service.impls2.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.model.entities.dto.UserDto;
import uz.sar7ar.springcore.service.impls2.TrainerService;
import uz.sar7ar.springcore.service.impls2.TrainingTypeService;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class o3_TrainerServiceTest {
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public o3_TrainerServiceTest(TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Test
    @Transactional(readOnly = true)
    void getTrainerByUserName() {
//        ('Lyda', 'Saffill', 'Lyda.Saffill', '3T27IvQ833', false); user_id = 99;
        TrainerDto trainer = trainerService.getTrainerByUserName("Lyda.Saffill").get();

        assertEquals("Lyda", trainer.getUser().getFirstName());
        assertEquals("Saffill", trainer.getUser().getLastName());
        assertEquals("Lyda.Saffill", trainer.getUser().getUserName());
        assertEquals("3T27IvQ833", trainer.getUser().getPassword());
        assertEquals(19, trainer.getId());
        assertEquals(99, trainer.getUser().getId());
    }

//    @Test
//    void createTrainer() {
//        UserDto newUserDto = new UserDto(null,
//                                  "newTrainer",
//                                  "testTrainer",
//                                  null,
//                                  null,
//                                  false);
//        TrainingTypeDto specialization = trainingTypeService.findTrainingTypeById(3).get();
//        TrainerDto newTrainer = new TrainerDto(specialization, newUserDto);
//        TrainerDto createdTrainer = trainerService.createTrainer(newTrainer);
//        assertEquals("newTrainer", createdTrainer.getUser().getFirstName());
//        assertEquals("testTrainer", createdTrainer.getUser().getLastName());
//        assertEquals("newTrainer.testTrainer", createdTrainer.getUser().getUserName());
//        assertFalse(createdTrainer.getUser().getIsActive());
//        assertEquals(TrainingTypeEnum.YOGA, createdTrainer.getSpecialization().getTrainingTypeName());
//    }

    @Test
    void updateTrainer() {
        TrainerDto trainer = trainerService.getTrainerByUserName("Merrel.Proby").get();
        TrainingTypeDto specialization  = trainingTypeService.findTrainingTypeById(1).get();
        trainer.getUser().setFirstName("updatedfn");
        trainer.getUser().setLastName("updatedln");
        trainer.getUser().setIsActive(true);
        trainer.setSpecialization(specialization);
         TrainerDto updatedTrainer = trainerService.updateTrainer(trainer);

        assertEquals("updatedfn", updatedTrainer.getUser().getFirstName());
        assertEquals("updatedln", updatedTrainer.getUser().getLastName());
        assertEquals("updatedfn.updatedln", updatedTrainer.getUser().getUserName());
        assertTrue(updatedTrainer.getUser().getIsActive());
        assertEquals(TrainingTypeEnum.RESISTANCE, updatedTrainer.getSpecialization().getTrainingTypeName());
    }


    @Test
    void deleteTrainerByUserName() {
//        Aimee.Zmitrovich id=96
//        Jude.Spinley 90
        Optional<TrainerDto> optTrainer = trainerService.getTrainerByUserName("Jude.Spinley");
        assertTrue(optTrainer.isPresent());
        trainerService.deleteTrainerByUserName("Jude.Spinley");
        Optional<TrainerDto> optTrainer2 = trainerService.getTrainerByUserName("Jude.Spinley");
        assertTrue(optTrainer2.isEmpty());
    }

    @Test
    @Transactional(readOnly = true)
    void getTrainersWhoseNotAssignedToTrainee() throws UserNameNotFoundException {
//        Vida.Lygo id=37
        Set<TrainerDto> trainers = trainerService.getTrainersWhoseNotAssignedToTrainee("Vida.Lygo");
        assertFalse(trainers.isEmpty());
    }
}