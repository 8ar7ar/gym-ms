package uz.sar7ar.springcore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.entities.Trainee;
import uz.sar7ar.springcore.model.entities.Trainer;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    @Query("SELECT t FROM Trainer AS t WHERE t.user.userName = :trainerUserName")
    Optional<Trainer> findTrainerByUserName(@Param("trainerUserName") String trainerUserName);

    @Query("SELECT t FROM Trainer AS t " +
            "WHERE t.user.isActive = true AND :traineeUserName " +
            "NOT IN (SELECT trainee.user.userName FROM t.trainees AS trainee)")
    Set<Trainer> TrainersWhoseNotAssignedToTrainee(@Param("traineeUserName") String traineeUserName);

    @Modifying
    @Query("DELETE FROM Trainer AS t " +
            "WHERE t.user.id = (SELECT u.id FROM User AS u " +
                               "WHERE u.userName = :userName)")
    void deleteTrainerByUserName(@Param("userName") String userName);

    @Query("SELECT t FROM Trainee AS t " +
            "JOIN t.trainers AS tr " +
            "WHERE tr.user.userName = :userName")
    Set<Trainee> getTrainersTrainees(@Param("userName") String userName);
}