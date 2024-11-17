package uz.sar7ar.springcore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.entities.Training;
import uz.sar7ar.springcore.model.entities.TrainingType;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    @Query("SELECT t FROM Training  AS t WHERE t.trainee.user.userName = :traineeUserName")
    List<Training> findTraineesAllTrainings(@Param("traineeUserName") String traineeUserName);

    @Query("SELECT t FROM Training AS t " +
            "WHERE t.trainee.user.userName = :traineeUserName " +
            "AND t.trainingDate >= :after")
    List<Training> findTraineesTrainingsAfter(@Param("traineeUserName") String traineeUserName,
                                              @Param("after") LocalDate after);

    @Query("SELECT t FROM Training AS t " +
            "WHERE t.trainee.user.userName = :traineeUserName " +
            "AND t.trainingDate <= :before")
    List<Training> findTraineesTrainingsBefore(@Param("traineeUserName") String traineeUserName,
                                               @Param("before") LocalDate before);

    @Query("SELECT t FROM Training AS t " +
            "WHERE t.trainee.user.userName = :traineeUserName " +
            "AND t.trainer.user.userName = :trainerUserName")
    List<Training> findTraineesTrainingsByTrainerUserName(@Param("traineeUserName") String traineeUserName,
                                                          @Param("trainerUserName") String trainerUserName);

    @Query("SELECT t FROM Training AS t " +
            "WHERE t.trainee.user.userName = :traineeUserName " +
            "AND t.trainingType = :trainingType")
    List<Training> findTraineesTrainingsByTrainingType(@Param("traineeUserName") String traineeUserName,
                                                       @Param("trainingType") TrainingType trainingType);

    @Query("SELECT t FROM Training  AS t WHERE t.trainer.user.userName = :trainerUserName")
    List<Training> findTrainersAllTrainings(@Param("trainerUserName") String trainerUserName);

    @Query("SELECT t FROM Training AS t " +
            "WHERE t.trainer.user.userName = :trainerUserName " +
            "AND t.trainee.user.userName = :traineeUserName")
    List<Training> findTrainersTrainingsByTraineeUserName(@Param("trainerUserName") String trainerUserName,
                                                          @Param("traineeUserName") String traineeUserName);

    @Query("SELECT t FROM Training AS t " +
            "WHERE t.trainer.user.userName = :trainerUserName " +
            "AND t.trainingDate >= :after")
    List<Training> findTrainersTrainingsAfter(@Param("trainerUserName") String trainerUserName,
                                              @Param("after") LocalDate after);

    @Query("SELECT t FROM Training  AS t " +
            "WHERE t.trainer.user.userName = :trainerUserName " +
            "AND t.trainingDate <= :before")
    List<Training> findTrainersTrainingsBefore(@Param("trainerUserName") String trainerUserName,
                                               @Param("before") LocalDate before);
}