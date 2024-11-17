package uz.sar7ar.springcore.service.impls2.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.service.impls2.TrainingTypeService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class o5_TrainingTypeServiceTest {
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public o5_TrainingTypeServiceTest(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Test
    void findTrainingTypeByName() {
        Optional<TrainingTypeDto> trainingType = trainingTypeService.findTrainingTypeByName(TrainingTypeEnum.ZUMBA);
        assertEquals(trainingType.get().getTrainingTypeName(), TrainingTypeEnum.ZUMBA);
    }

    @Test
    void findTrainingTypeById() {
        Optional<TrainingTypeDto> trainingType = trainingTypeService.findTrainingTypeById(2);
        assertEquals(trainingType.get().getTrainingTypeName(), TrainingTypeEnum.STRETCHING);
    }
}