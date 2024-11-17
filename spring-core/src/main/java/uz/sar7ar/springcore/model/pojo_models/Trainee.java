package uz.sar7ar.springcore.model.pojo_models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class Trainee extends User {
    private Date dateOfBirth;
    private String address;
    private int traineeId;

    public Trainee(String firstName, String lastName, boolean isActive,
                   Date dateOfBirth, String address, int traineeId) {
        super(firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.traineeId = traineeId;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", traineeId=" + traineeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
