package uz.sar7ar.springcore.service.impls2.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.UserDto;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.TrainerService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class o2_TraineeServiceTest {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public o2_TraineeServiceTest(TraineeService traineeService,
                                 TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Test
    @Transactional(readOnly = true)
    void getTraineeByUserName() {
        //('Ezequiel', 'Kimmince', 'Ezequiel.Kimmince', 'Cx910EL870', false);
//        trainee (date_of_birth, address, user_id) values ('2010-07-16', 'Suite 4', 66);
        Optional<TraineeDto> optTrainee = traineeService.getTraineeByUserName("Ezequiel.Kimmince");
        assertTrue(optTrainee.isPresent());
        TraineeDto trainee = optTrainee.get();
        assertEquals("Ezequiel", trainee.getUser().getFirstName());
        assertEquals("Kimmince", trainee.getUser().getLastName());
        assertEquals("Ezequiel.Kimmince", trainee.getUser().getUserName());
        assertEquals("Cx910EL870", trainee.getUser().getPassword());
        assertFalse(trainee.getUser().getIsActive());
        assertEquals("Suite 4", trainee.getAddress());
        assertEquals(LocalDate.of(2010, 7, 16), trainee.getDateOfBirth());
    }

    @Test
    void createTrainee() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setIsActive(true);
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUser(userDto);
        traineeDto.setAddress("Address, address 11");
        traineeDto.setDateOfBirth(LocalDate.of(200, 1, 1));
        TraineeDto createdTrainee = traineeService.createTrainee(traineeDto);


        assertEquals("John", createdTrainee.getUser().getFirstName());
        assertEquals("Doe", createdTrainee.getUser().getLastName());
        assertEquals("John.Doe", createdTrainee.getUser().getUserName());
        assertEquals("Address, address 11", createdTrainee.getAddress());
        assertEquals(LocalDate.of(200, 1, 1), createdTrainee.getDateOfBirth());
    }

    @Test
    void updateTrainee() {
        TraineeDto traineeDto = traineeService.getTraineeByUserName("Justus.Sabberton").get();
        UserDto userDto  = traineeDto.getUser();
        userDto.setFirstName("Aaaa");
        userDto.setLastName("Bbbx");
        userDto.setIsActive(false);
        traineeDto.setDateOfBirth(LocalDate.of(1990,1,1));
        traineeDto.setAddress("Address, address 11");
        TraineeDto updatedtraineeDto = traineeService.updateTrainee(traineeDto);

        assertEquals("Aaaa", updatedtraineeDto.getUser().getFirstName());
        assertEquals("Bbbx", updatedtraineeDto.getUser().getLastName());
        assertEquals("Aaaa.Bbbx", updatedtraineeDto.getUser().getUserName());
        assertEquals(false, updatedtraineeDto.getUser().getIsActive());
        assertEquals("Address, address 11", updatedtraineeDto.getAddress());
        assertEquals(LocalDate.of(1990,1,1), updatedtraineeDto.getDateOfBirth());
    }

    @Test
    void deleteTraineeByUserName() {
//        ('Trenton', 'Meltetal', 'Trenton.Meltetal', 'tP576Ke619', false);  id=21
        assertTrue(traineeService.getTraineeByUserName("Trenton.Meltetal").isPresent());
        traineeService.deleteTraineeByUserName("Trenton.Meltetal");
        assertFalse(traineeService.getTraineeByUserName("Trenton.Meltetal").isPresent());
    }

    @Test
    void updateTraineeTrainersList() {
//        values ('Findley', 'Loutheane', 'Findley.Loutheane', '9q986fz774', true); id=78 5-trainers
//        ('Ingrid', 'Colcomb', 'Ingrid.Colcomb', 'WC77lC0812', true); id=91, trid=11 trainer
        Optional<TraineeDto> traineeDto = traineeService.getTraineeByUserName("Findley.Loutheane");
        Optional<TrainerDto> trainerToDeleteDTO = trainerService.getTrainerByUserName("Ingrid.Colcomb");

        assertTrue(traineeDto.isPresent());
        assertTrue(trainerToDeleteDTO.isPresent());

        TraineeDto trainee = traineeDto.get();
        TrainerDto trainerToDelete = trainerToDeleteDTO.get();

        Set<TrainerDto> trainers = trainee.getTrainers();
        trainers.forEach(System.out::println);

        boolean a = trainers.contains(trainerToDelete);
        assertTrue(trainers.contains(trainerToDelete));
        trainers.remove(trainerToDelete);
        traineeService.updateTraineeTrainersList(trainee, trainers);
        var b = traineeService.getTraineeByUserName("Findley.Loutheane").get().getTrainers();
        assertFalse(traineeService.getTraineeByUserName("Findley.Loutheane").get().getTrainers().contains(trainerToDelete));

    }

    @Test
    void assignTrainerToTrainee() {
//        9,true,fk58zqI413,Meredithe,Mustill,Meredithe.Mustill tainee id=9
//        90,true,5M40oid852,Jude,Spinley,Jude.Spinley id=90
        TraineeDto trainee = traineeService.getTraineeByUserName("Meredithe.Mustill").get();
        TrainerDto trainer = trainerService.getTrainerByUserName("Jude.Spinley").get();

        Set<TrainerDto> traineeTrainersBefore = trainee.getTrainers();

        assertEquals(2, traineeTrainersBefore.size());
        assertFalse(traineeTrainersBefore.stream()
                                         .map(trainer1 -> trainer1.getUser().getUserName())
                                         .toList()
                                         .contains("Jude.Spinley"));

        traineeService.assignTrainerToTrainee(trainee, trainer);

        Set<TrainerDto> traineeTrainersAfter = traineeService
                                .getTraineeByUserName("Meredithe.Mustill").get().getTrainers();

        assertEquals(3, traineeTrainersAfter.size());
        assertTrue(traineeTrainersAfter.stream()
                                       .map(trainer1 -> trainer1.getUser().getUserName())
                                       .toList()
                                       .contains("Jude.Spinley"));

    }
}