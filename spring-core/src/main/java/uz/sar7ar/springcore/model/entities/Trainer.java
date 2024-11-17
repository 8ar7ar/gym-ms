package uz.sar7ar.springcore.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @NotNull
    @ManyToOne() //cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH}
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Training> trainings = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id"))
    private Set<Trainee> trainees = new HashSet<>();


    public Trainer(Integer id, TrainingType specialization, User user) {
        this.id = id;
        this.specialization = specialization;
        this.user = user;
    }

    public TrainerDto toTrainerDto() {
        return new TrainerDto(id, user.toUserDto(), specialization.toTrainingTypeDto());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Trainer trainer = (Trainer) other;
        return Objects.equals(getId(), trainer.getId())
                && Objects.equals(getSpecialization(), trainer.getSpecialization())
                && Objects.equals(getUser(), trainer.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSpecialization(), getUser());
    }
}


