package uz.sar7ar.springcore.model.entities.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.sar7ar.springcore.model.entities.Training;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Training}
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainingDto implements Serializable {
    private Integer id;

    @Setter
    @NotNull
    private TraineeDto trainee;

    @Setter
    @NotNull
    private TrainerDto trainer;

    @Setter
    @NotNull
    @Size(min = 5, max = 63)
    private String trainingName;

    @Setter
    @NotNull
    private TrainingTypeDto trainingType;

    @Setter
    @FutureOrPresent
    private LocalDate trainingDate;

    @Setter
    @NotNull
    @Min(1)
    private Integer duration;

    public TrainingDto(TraineeDto trainee,
                       TrainerDto trainer,
                       String trainingName,
                       TrainingTypeDto trainingType,
                       LocalDate trainingDate,
                       Integer duration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }
}