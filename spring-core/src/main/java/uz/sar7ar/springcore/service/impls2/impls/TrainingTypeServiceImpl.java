package uz.sar7ar.springcore.service.impls2.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.TrainingType;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;
import uz.sar7ar.springcore.repository.jpa.TrainingTypeRepository;
import uz.sar7ar.springcore.service.impls2.TrainingTypeService;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingTypeDto> findTrainingTypeByName(TrainingTypeEnum trainingTypeName) {
        log.info("Finding training type by name [{}]", trainingTypeName);
        Optional<TrainingType> optTrainingTypeDto = trainingTypeRepository.findByTrainingTypeName(trainingTypeName);
        log.info("Returning Optional of training type by name [{}]", trainingTypeName);

        return optTrainingTypeDto.map(TrainingType::toTrainingTypeDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingTypeDto> findTrainingTypeById(Integer id) {
        log.info("Finding training type by traineeId [{}]", id);
        Optional<TrainingType> optTrainingType = trainingTypeRepository.findById(id);
        log.info("Returning training type by traineeId [{}]", id);

        return optTrainingType.map(TrainingType::toTrainingTypeDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeDto> findAllTrainingTypes() {

        return trainingTypeRepository.findAll()
                                     .stream()
                                     .map(TrainingType::toTrainingTypeDto)
                                     .toList();
    }
}
