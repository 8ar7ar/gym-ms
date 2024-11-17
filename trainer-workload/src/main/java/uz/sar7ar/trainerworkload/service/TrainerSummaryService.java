package uz.sar7ar.trainerworkload.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.sar7ar.trainerworkload.model.mongodb.TrainersTrainingSummary;
import uz.sar7ar.trainerworkload.repository.mongo.TrainerSummaryRepository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing trainer summary-related operations.
 */
@AllArgsConstructor
@Service
public class TrainerSummaryService {
    private static final Logger transactionLogger = LoggerFactory.getLogger("TransactionLogger");
    private static final Logger operationLogger = LoggerFactory.getLogger(TrainerSummaryService.class);

    private TrainerSummaryRepository trainerSummaryRepository;


    /**
     * Processes an event for a trainer. If the trainer doesn't exist, creates a new document in MongoDB.
     * If the trainer exists, updates the existing document.
     *
     * @param trainerUserName the username of the trainer
     * @param firstName the first name of the trainer
     * @param lastName the last name of the trainer
     * @param isActive whether the trainer is active
     * @param trainingDate the date of the training
     * @param trainingDurationInHours the duration of the training in hours
     */
    public void  processTrainerEvent(String trainerUserName,
                                      String firstName,
                                      String lastName,
                                      boolean isActive,
                                      LocalDate trainingDate,
                                      int trainingDurationInHours){
        operationLogger.info("Starting event processing for trainer: {}", trainerUserName);
        Optional<TrainersTrainingSummary> optionalTrainersTrainingSummary = trainerSummaryRepository.findByUserName(trainerUserName);
        if (optionalTrainersTrainingSummary.isEmpty()) {
            operationLogger.info("Trainer not found. Creating a new record for {}", trainerUserName);
            TrainersTrainingSummary newSummary = createNewTrainerSummary(trainerUserName, firstName, lastName, isActive, trainingDate, trainingDurationInHours);
            trainerSummaryRepository.save(newSummary);
        } else {
            TrainersTrainingSummary trainersTrainingSummary = optionalTrainersTrainingSummary.get();
            updateTrainerSummary(trainersTrainingSummary, trainingDate, trainingDurationInHours);
            trainerSummaryRepository.save(trainersTrainingSummary);
            operationLogger.info("Trainer summary updated successfully for {}", trainerUserName);
        }
        System.out.println("Trainer event processed and persisted in MongoDB");

    }

    /**
     * Creates a new {@link TrainersTrainingSummary} from the given parameters.
     *
     * @param trainerUserName the username of the trainer
     * @param firstName the first name of the trainer
     * @param lastName the last name of the trainer
     * @param isActive whether the trainer is active
     * @param trainingDate the date of the training
     * @param trainingDurationInHours the duration of the training in hours
     *
     * @return a new {@link TrainersTrainingSummary}
     */
    private TrainersTrainingSummary createNewTrainerSummary(String trainerUserName, String firstName, String lastName, boolean isActive, LocalDate trainingDate, int trainingDurationInHours) {
        TrainersTrainingSummary trainerSummary = new TrainersTrainingSummary();
        trainerSummary.setUserName(trainerUserName);
        trainerSummary.setFirstName(firstName);
        trainerSummary.setLastName(lastName);
        trainerSummary.setStatus(isActive);

        TrainersTrainingSummary.YearSummary yearSummary = new TrainersTrainingSummary.YearSummary();
        yearSummary.setYear(trainingDate.getYear());
        yearSummary.setMonths(Map.of(getMonthName(trainingDate), trainingDurationInHours));

        trainerSummary.setYears(List.of(yearSummary));

        return trainerSummary;
    }

    /**
     * Updates the given {@link TrainersTrainingSummary} to reflect the new training.
     *
     * @param trainerSummary the summary to update
     * @param trainingDate the date of the training
     * @param trainingDurationInHours the duration of the training in hours
     */
    private void updateTrainerSummary(TrainersTrainingSummary trainerSummary, LocalDate trainingDate, int trainingDurationInHours) {
        int year = trainingDate.getYear();
        String month = getMonthName(trainingDate);

        Optional<TrainersTrainingSummary.YearSummary> yearSummaryOptional = trainerSummary.getYears().stream()
                .filter(y -> y.getYear() == year)
                .findFirst();

        if (yearSummaryOptional.isPresent()) {
            TrainersTrainingSummary.YearSummary yearSummary = yearSummaryOptional.get();
            yearSummary.getMonths().put(month, yearSummary.getMonths().getOrDefault(month, 0) + trainingDurationInHours);
        } else {
            TrainersTrainingSummary.YearSummary newYearSummary = new TrainersTrainingSummary.YearSummary();
            newYearSummary.setYear(year);
            newYearSummary.setMonths(Map.of(month, trainingDurationInHours));
            trainerSummary.getYears().add(newYearSummary);
        }
    }

    /**
     * Returns the full, English name of the month of the given date.
     *
     * @param trainingDate the date to get the month name for
     * @return the full, English name of the month
     */
    private String getMonthName(LocalDate trainingDate) {
        return trainingDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
