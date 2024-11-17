package uz.sar7ar.trainerworkload.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainingSession> trainingSessions = new ArrayList<>();

    public Trainer(String userName, String firstName, String lastName, boolean isActive) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }
}
