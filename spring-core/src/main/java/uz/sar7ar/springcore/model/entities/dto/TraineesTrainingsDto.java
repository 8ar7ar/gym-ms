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
public class TraineesTrainingsDto {
    private String trainingName;

    private String trainerUserName;

    private LocalDate trainingDate;

    private TrainingTypeEnum trainingType;

    private Integer trainingDuration;

}
