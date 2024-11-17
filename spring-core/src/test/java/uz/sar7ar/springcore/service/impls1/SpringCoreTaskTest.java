package uz.sar7ar.springcore.service.impls1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uz.sar7ar.springcore.model.pojo_models.Trainee;
import uz.sar7ar.springcore.model.pojo_models.Trainer;
import uz.sar7ar.springcore.model.pojo_models.Training;
import uz.sar7ar.springcore.model.pojo_models.TrainingType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SpringCoreTaskTest {
    @Autowired
    private FacadeService facadeService;


    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createTrainee() {
        int newTraineeId = 187;
        Trainee newTrainee = new Trainee(
                "Sarvar",
                "Saloxiddinov",
                false,
                new Date(1995, Calendar.AUGUST, 8),
                "Fergana, Oltiariq, Tinchlik 11",
                newTraineeId);
        // if trainee does not exist in Map memory with given ID Map<> returns (old value with given key) null ()
        facadeService.createTrainee(newTrainee);

        Trainee createdTraineeFromStorage = facadeService.getTrainee(newTraineeId);

        assertLinesMatch(
                List.of(new String[]{
                        newTrainee.getFirstName(),
                        newTrainee.getLastName(),
                        newTrainee.getUsername(),
                        newTrainee.getPassword(),
                        String.valueOf(newTrainee.getIsActive()),
                        newTrainee.getDateOfBirth().toString(),
                        newTrainee.getAddress(),
                        String.valueOf(newTrainee.getTraineeId())}
                ),

                List.of(new String[]{
                        createdTraineeFromStorage.getFirstName(),
                        createdTraineeFromStorage.getLastName(),
                        createdTraineeFromStorage.getUsername(),
                        createdTraineeFromStorage.getPassword(),
                        String.valueOf(newTrainee.getIsActive()),
                        createdTraineeFromStorage.getDateOfBirth().toString(),
                        createdTraineeFromStorage.getAddress(),
                        String.valueOf(newTrainee.getTraineeId())}
                )
        );
    }

    @Test
    void updateTrainee() {
        Random random = new Random();
        int traineeId = random.nextInt(99);

        String updFirstName = "updatedName";
        String updLastName = "updatedLastName";
        String updUsername = "updatedName.updatedLastName";
        String updPassword = "password12345";
        boolean updIsActive = false;
        Date updDateOfBirth = new Date(2024, 1, 1);
        String updAddress = "updated address, 001";

        Trainee toBeUpdated = facadeService.getTrainee(traineeId);
        toBeUpdated.setFirstName(updFirstName);
        toBeUpdated.setLastName(updLastName);
        toBeUpdated.setPassword(updPassword);
        toBeUpdated.setIsActive(updIsActive);
        toBeUpdated.setDateOfBirth(updDateOfBirth);
        toBeUpdated.setAddress(updAddress);

        facadeService.updateTrainee(toBeUpdated);
        Trainee justUpdatedTrainee = facadeService.getTrainee(traineeId);


        assertLinesMatch(
                List.of(new String[]{
                        updFirstName,
                        updLastName,
                        updUsername,
                        updPassword,
                        String.valueOf(updIsActive),
                        updDateOfBirth.toString(),
                        updAddress}
                ),

                List.of(new String[]{
                        justUpdatedTrainee.getFirstName(),
                        justUpdatedTrainee.getLastName(),
                        justUpdatedTrainee.getUsername(),
                        justUpdatedTrainee.getPassword(),
                        String.valueOf(justUpdatedTrainee.getIsActive()),
                        justUpdatedTrainee.getDateOfBirth().toString(),
                        justUpdatedTrainee.getAddress()}
                )
        );
    }

    @Test
    void deleteTrainee() {
        Random random = new Random();
        int traineeId = random.nextInt(99);
        facadeService.deleteTrainee(traineeId);
        assertNull(facadeService.getTrainee(traineeId));
    }

    @Test
    void getTrainee() {
        int traineeId = 9;
//        {"firstName":"Stillman","lastName":"Jaffrey","isActive":false,
//        "dateOfBirth":"2006-11-08","address":"5 Lindbergh Court","traineeId":9}

        Trainee trainee = facadeService.getTrainee(traineeId);
        assertEquals(9, trainee.getTraineeId());
        assertEquals("Stillman", trainee.getFirstName());
        assertEquals("Jaffrey", trainee.getLastName());
        assertEquals("Stillman.Jaffrey", trainee.getUsername());
        assertEquals("5 Lindbergh Court", trainee.getAddress());
        assertFalse(trainee.getIsActive()); // "isActive":false
        assertEquals(LocalDate.of(2006, 11, 8),
                LocalDate.ofInstant(trainee.getDateOfBirth().toInstant(), ZoneId.systemDefault()));
    }

    @Test
    void createTrainer() {
        int trainer1Id = 31;
        int trainer2Id = 32;
        Trainer trainer1 = new Trainer(
                "John", "Doe", true,
                TrainingType.BALANCE_TRAINING, trainer1Id);
        Trainer trainer2 = new Trainer(
                "John", "Doe", false,
                TrainingType.BALANCE_TRAINING, trainer2Id);

        facadeService.createTrainer(trainer1);
        facadeService.createTrainer(trainer2);

        Trainer justCreatedTrainer1 = facadeService.getTrainer(trainer1Id);
        Trainer justCreatedTrainer2 = facadeService.getTrainer(trainer2Id);

        assertNotNull(justCreatedTrainer1);
        assertNotNull(justCreatedTrainer2);

        assertLinesMatch(
                List.of(new String[]{
                        String.valueOf(trainer1.getTrainerId()),
                        trainer1.getFirstName(),
                        trainer1.getLastName(),
                        trainer1.getFirstName() + "." + trainer1.getLastName(),
                        String.valueOf(trainer1.getIsActive()),
                        trainer1.getSpecialization().toString()}
                ),
                List.of(new String[]{
                        String.valueOf(justCreatedTrainer1.getTrainerId()),
                        justCreatedTrainer1.getFirstName(),
                        justCreatedTrainer1.getLastName(),
                        justCreatedTrainer1.getUsername(),
                        String.valueOf(justCreatedTrainer1.getIsActive()),
                        justCreatedTrainer1.getSpecialization().toString()}
                )
        );
        assertLinesMatch(
                List.of(new String[]{
                                String.valueOf(trainer2.getTrainerId()),
                                trainer2.getFirstName(),
                                trainer2.getLastName(),
                                trainer2.getFirstName() + "." + trainer2.getLastName() + "1",
                                String.valueOf(trainer2.getIsActive()),
                                trainer2.getSpecialization().toString()}
                ),
                List.of(new String[]{
                                String.valueOf(justCreatedTrainer2.getTrainerId()),
                                justCreatedTrainer2.getFirstName(),
                                justCreatedTrainer2.getLastName(),
                                justCreatedTrainer2.getUsername(),
                                String.valueOf(justCreatedTrainer2.getIsActive()),
                                justCreatedTrainer2.getSpecialization().toString()}
                )
        );
    }

    @Test
    void updateTrainer() {
        int trainerId = 1;
        String updFirstName = "Max";
        String updLastName = "Plank";
        boolean updIsActive = false;
        TrainingType updSpecialization = TrainingType.FUNCTIONAL_TRAINING;

        //[{"firstName":"Auroora","lastName":"Boate","isActive":true,
        // "specialization":"AGILITY_TRAINING","trainerId":1}
        Trainer oldTrainer = facadeService.getTrainer(trainerId);
        assertNotNull(oldTrainer);

        oldTrainer.setFirstName(updFirstName);
        oldTrainer.setLastName(updLastName);
        oldTrainer.setIsActive(updIsActive);
        oldTrainer.setSpecialization(updSpecialization);
        facadeService.updateTrainer(oldTrainer);
        Trainer updatedTrainer = facadeService.getTrainer(trainerId);

        assertLinesMatch(
                List.of(new String[]{
                        updFirstName,
                        updLastName,
                        updFirstName + "." + updLastName,
                        String.valueOf(updIsActive),
                        updSpecialization.toString()}
                ),
                List.of(new String[]{
                        updatedTrainer.getFirstName(),
                        updatedTrainer.getLastName(),
                        updatedTrainer.getUsername(),
                        String.valueOf(updatedTrainer.getIsActive()),
                        updatedTrainer.getSpecialization().toString()}
                )
        );
    }

    @Test
    void getTrainer() {
//        {"firstName":"Vladimir","lastName":"Milligan","isActive":true,
//        "specialization":"STRENGTH_TRAINING","trainerId":13},
        int trainerId = 13;
        Trainer trainer = facadeService.getTrainer(trainerId);

        assertEquals(trainerId, trainer.getTrainerId());
        assertEquals("Vladimir", trainer.getFirstName());
        assertEquals("Milligan", trainer.getLastName());
        assertEquals("Vladimir.Milligan", trainer.getUsername());
        assertEquals("STRENGTH_TRAINING", trainer.getSpecialization().toString());
        assertTrue(trainer.getIsActive()); // "isActive":true
    }

    @Test
    void createTraining() {
        int traineeId = 1;
        int trainerId = 3;
        String trainingName = "personalTraining";
        TrainingType type = TrainingType.WEIGHTLIFTING;
        Date startDate = new Date(2024, Calendar.JULY, 17);
        int duration = 90;

        Training training = new Training(traineeId, trainerId, trainingName, type, startDate, duration);
        facadeService.createTraining(training);
        Training justCreatedTraining = facadeService.getTraining(trainingName);

        assertNotNull(justCreatedTraining);
        assertEquals(traineeId, justCreatedTraining.getTraineeId());
        assertEquals(trainerId, justCreatedTraining.getTrainerId());
        assertEquals(trainingName, justCreatedTraining.getTrainingName());
        assertEquals(type, justCreatedTraining.getTrainingType());
        assertEquals(startDate.toString(), justCreatedTraining.getTrainingDate().toString());
        assertEquals(duration, justCreatedTraining.getDuration());
    }

    @Test
    void getTraining() {
//        {"traineeId":18,"trainerId":12,"trainingName":"eros vestibulum",
//        "trainingType":"MOBILITY_TRAINING","trainingDate":"2024-01-22","duration":44},
        String trainingName = "eros vestibulum";

        Training training = facadeService.getTraining(trainingName);
        assertEquals(18,training.getTraineeId());
        assertEquals(12, training.getTrainerId());
        assertEquals(trainingName, training.getTrainingName());
        assertEquals(TrainingType.MOBILITY_TRAINING, training.getTrainingType());
//        assertEquals(new Date(2024, Calendar.JANUARY,22).toString(), training.getTrainingDate().toString());
        assertEquals(44, training.getDuration());
    }
}