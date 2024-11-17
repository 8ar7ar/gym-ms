package uz.sar7ar.springcore.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import uz.sar7ar.springcore.model.entities.dto.TrainingDto;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @NotNull
    @ManyToOne() //cascade = CascadeType.ALL
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @NotNull
    @Size(min = 5, max = 63)
    @Column(name = "training_name", nullable = false, length = 63)
    private String trainingName;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;


    @FutureOrPresent
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @NotNull
    @Min(1)
    @Column(name = "duration", nullable = false)
    private Integer duration;

    public Training(Trainee trainee, Trainer trainer,
                    String trainingName, TrainingType trainingType,
                    LocalDate trainingDate, Integer duration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }

    public TrainingDto toTrainingDto() {
        return new TrainingDto(id, trainee.toTraineeDto(), trainer.toTrainerDto(),
                trainingName, trainingType.toTrainingTypeDto(), trainingDate, duration);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Training training = (Training) other;
        return Objects.equals(getId(), training.getId())
                && Objects.equals(getTrainee(), training.getTrainee())
                && Objects.equals(getTrainer(), training.getTrainer())
                && Objects.equals(getTrainingName(), training.getTrainingName())
                && Objects.equals(getTrainingType(), training.getTrainingType())
                && Objects.equals(getTrainingDate(), training.getTrainingDate())
                && Objects.equals(getDuration(), training.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTrainee(), getTrainer(), getTrainingName(),
                            getTrainingType(), getTrainingDate(), getDuration());
    }

}
