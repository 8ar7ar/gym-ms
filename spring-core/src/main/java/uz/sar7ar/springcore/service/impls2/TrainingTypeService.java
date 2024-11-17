package uz.sar7ar.springcore.service.impls2;

import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {

    Optional<TrainingTypeDto> findTrainingTypeByName(TrainingTypeEnum trainingTypeName);

    Optional<TrainingTypeDto> findTrainingTypeById(Integer id);

    List<TrainingTypeDto> findAllTrainingTypes();
}
