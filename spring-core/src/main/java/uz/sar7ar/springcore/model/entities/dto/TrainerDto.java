package uz.sar7ar.springcore.model.entities.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.sar7ar.springcore.model.entities.Trainer;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for {@link Trainer}
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainerDto implements Serializable {
    private Integer id;

    @Setter
    @NotNull
    private TrainingTypeDto specialization;

    @Setter
    @NotNull
    private UserDto user;

    @Setter
    private Set<TraineeDto> trainees;

    public TrainerDto(UserDto user, TrainingTypeDto specialization,  Set<TraineeDto> trainees) {
        this.user = user;
        this.specialization = specialization;
        this.trainees = trainees;
    }

    public TrainerDto(TrainingTypeDto specialization, UserDto user) {
        this.user = user;
        this.specialization = specialization;
        this.trainees = new HashSet<>();
    }

    public TrainerDto(Integer id, UserDto user, TrainingTypeDto specialization) {
        this.id = id;
        this.user = user;
        this.specialization = specialization;
    }
}