package uz.sar7ar.trainerworkload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sar7ar.trainerworkload.model.TrainingSession;

/**
 * Repository for managing {@link TrainingSession} entities.
 */
@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
}
