package uz.sar7ar.trainerworkload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sar7ar.trainerworkload.model.Trainer;

import java.util.Optional;

/**
 * Repository for managing {@link Trainer} entities.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    /**
     * Finds a trainer by their username.
     *
     * @param username the username of the trainer.
     * @return an optional containing the trainer if found, or an empty optional if not.
     */
    Optional<Trainer> findByUserName(String username);
}
