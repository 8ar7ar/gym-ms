package uz.sar7ar.trainerworkload.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingDtoMsg implements Serializable {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    @JsonProperty("isActive")
    private boolean isActive;
    private LocalDate trainingDate;
    private int durationInHours;
    private String actionType;
}
