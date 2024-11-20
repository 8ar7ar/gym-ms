package uz.sar7ar.trainerworkload.model.mongodb;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "trainer_summaries")
@CompoundIndex(name = "trainer_name_index",
               def = "{'first_name': 1, 'last_name': 1}")
public class TrainersTrainingSummary {
    @Id
    private String id;

    @Field("user_name")
    private String userName;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("status")
    private boolean status;

    @Field("years")
    private List<YearSummary> years;

    /**
 * Represents a summary of a trainer's training activities for a specific year.
 * This class is used to store and retrieve data from the MongoDB collection 'trainer_summaries'.
 *
 * @author Tabnine
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public static class YearSummary {

    /**
     * The year for which the training summary is provided.
     */
    private int year;

    /**
     * A map representing the number of training activities for each month in the specified year.
     * The keys are the month names (e.g., "January", "February", etc.), and the values are the corresponding counts.
     */
    private Map<String, Integer> months;
}
}
