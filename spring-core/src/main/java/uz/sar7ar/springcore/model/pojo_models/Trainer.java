package uz.sar7ar.springcore.model.pojo_models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Trainer extends User {
    private TrainingType specialization;
    private int trainerId;

    public Trainer(String firstName, String lastName, boolean isActive,
                   TrainingType specialization, int trainerId) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
        this.trainerId = trainerId;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization +
                ", trainerId=" + trainerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}

