package uz.sar7ar.springcore.model.entities.dto;

import lombok.*;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainersTrainingsDto {
    private String trainingName;

    private String traineeUserName;

    private LocalDate trainingDate;

    private TrainingTypeEnum trainingType;

    private Integer trainingDuration;
}
