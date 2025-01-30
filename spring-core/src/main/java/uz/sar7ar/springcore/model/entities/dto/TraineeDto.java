package uz.sar7ar.springcore.model.entities.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TraineeDto implements Serializable {
    private Integer id;

    @Setter
    @Past
    private LocalDate dateOfBirth;

    @Setter
    @Size(message = "Address length must be between 5 and 255", min = 5, max = 255)
    private String address;

    @Setter
    @NotNull
    private UserDto user;

    @Setter
    private Set<TrainerDto> trainers;

    public TraineeDto(LocalDate dateOfBirth, String address, UserDto user) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }
}