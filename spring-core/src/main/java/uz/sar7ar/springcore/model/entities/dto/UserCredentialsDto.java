package uz.sar7ar.springcore.model.entities.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserCredentialsDto {
    @NotNull
    private String userName;

    @NotNull
    private String password;
}
