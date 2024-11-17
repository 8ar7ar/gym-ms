package uz.sar7ar.springcore.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.sar7ar.springcore.model.entities.dto.UserDto;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Size(min = 3, max = 30,
            message = "Length of firstname must be between 3 and 31")
    @Column(name = "first_name", nullable = false, length = 31)
    private String firstName;

    @Size(min = 3, max = 30,
            message = "Length of lastname must be between 3 and 31")
    @Column(name = "last_name", nullable = false, length = 31)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false, length = 63)
    private String userName;

//    @Size(min = 10, max = 10)
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public UserDto toUserDto() {
        return new UserDto(id, firstName, lastName, userName, password, role, isActive);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        User user = (User) other;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getFirstName(), user.getFirstName())
                && Objects.equals(getLastName(), user.getLastName())
                && Objects.equals(getUserName(), user.getUserName())
                && Objects.equals(getPassword(), user.getPassword())
                && Objects.equals(getIsActive(), user.getIsActive());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(),
                            getUserName(), getPassword(), getIsActive());
    }
}
