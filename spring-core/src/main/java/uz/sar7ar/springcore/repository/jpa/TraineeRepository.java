package uz.sar7ar.springcore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.sar7ar.springcore.model.entities.Trainee;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {

    @Query("SELECT t FROM Trainee AS t WHERE t.user.userName = :userName")
    Optional<Trainee> findTraineeByUserName(@Param("userName") String userName);

    @Modifying
    @Query("DELETE FROM Trainee AS t " +
            "WHERE t.user.id = (SELECT u.id FROM User AS u " +
                               "WHERE u.userName = :userName)")
    void deleteTraineeByUserName(@Param("userName") String userName);

}