package uz.sar7ar.springcore.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.sar7ar.springcore.model.entities.dto.TrainingTypeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "training_type")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type_name", nullable = false, length = 31)
    private TrainingTypeEnum trainingTypeName;

    @ToString.Exclude
    @OneToMany(mappedBy = "specialization",orphanRemoval = true,
               fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<Trainer> trainers = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "trainingType",orphanRemoval = true,
               fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<Training> trainings = new ArrayList<>();

    public TrainingType(Integer id, TrainingTypeEnum trainingTypeName) {
        this.id = id;
        this.trainingTypeName = trainingTypeName;
    }

    public TrainingTypeDto toTrainingTypeDto(){
        return new TrainingTypeDto(id, trainingTypeName);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        TrainingType that = (TrainingType) other;
        return Objects.equals(getId(), that.getId())
                && getTrainingTypeName() == that.getTrainingTypeName()
                && Objects.equals(getTrainers(), that.getTrainers())
                && Objects.equals(getTrainings(), that.getTrainings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTrainingTypeName(),
                            getTrainers(), getTrainings());
    }
}

