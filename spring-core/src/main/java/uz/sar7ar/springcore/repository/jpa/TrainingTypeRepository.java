package uz.sar7ar.springcore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.entities.TrainingType;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
    @Query("SELECT tt FROM TrainingType AS tt " +
            "WHERE tt.trainingTypeName =:trainingType")
    Optional<TrainingType> findByTrainingTypeName(@Param("trainingType") TrainingTypeEnum trainingType);
}