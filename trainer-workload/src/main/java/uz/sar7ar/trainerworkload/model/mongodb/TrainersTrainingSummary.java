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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class YearSummary{
        private int year;
        private Map<String, Integer> months;
    }
}
