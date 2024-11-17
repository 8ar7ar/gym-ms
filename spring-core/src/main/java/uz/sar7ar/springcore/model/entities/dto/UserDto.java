package uz.sar7ar.springcore.model.entities.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import uz.sar7ar.springcore.model.entities.Roles;
import uz.sar7ar.springcore.model.entities.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto implements Serializable {
    private Integer id;

    @Setter
    @Size(message = "Length of firstname must be between 3 and 31", min = 3, max = 30)
    private String firstName;

    @Setter
    @Size(message = "Length of lastname must be between 3 and 31", min = 3, max = 30)
    private String lastName;

    @Size(min = 7, max = 63)
    private String userName;

    @Setter
    @Size(min = 10, max = 10)
    private String password;

    @Setter
    private Roles role;

    @Setter
    private Boolean isActive;

    public UserDto(String firstName, String lastName, Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}