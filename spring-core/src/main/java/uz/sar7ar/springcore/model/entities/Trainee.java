package uz.sar7ar.springcore.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainee")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(min = 5, max = 255, message = "Address length must be between 5 and 255")
    @Column(name = "address")
    private String address;

    @NotNull
    @OneToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "trainee", fetch = FetchType.EAGER,
               cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Training> trainings = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "trainees",
                cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Trainer> trainers = new HashSet<>();

    public TraineeDto toTraineeDto() {
        return new TraineeDto(id, dateOfBirth, address, user.toUserDto(),
                trainers.stream().map(Trainer::toTrainerDto).collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Trainee trainee = (Trainee) other;
        return Objects.equals(getId(), trainee.getId())
                && Objects.equals(getDateOfBirth(), trainee.getDateOfBirth())
                && Objects.equals(getAddress(), trainee.getAddress())
                && Objects.equals(getUser(), trainee.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateOfBirth(), getAddress(), getUser());
    }
}


