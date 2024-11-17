package uz.sar7ar.springcore.model.pojo_models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class Training {
    private int traineeId;
    private int trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDate;
    private int duration;

    public Training(int traineeId, int trainerId, String trainingName,
                    TrainingType trainingType, Date trainingDate, int duration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", duration=" + duration +
                '}';
    }
}
