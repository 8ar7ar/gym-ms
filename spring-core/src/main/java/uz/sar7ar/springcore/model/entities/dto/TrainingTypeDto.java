package uz.sar7ar.springcore.model.entities.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.sar7ar.springcore.model.entities.TrainingType;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainingTypeDto implements Serializable {
    private Integer id;

    @Setter
    @NotNull
    private TrainingTypeEnum trainingTypeName;

    public TrainingTypeDto(TrainingTypeEnum trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}