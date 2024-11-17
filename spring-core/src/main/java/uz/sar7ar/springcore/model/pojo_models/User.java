package uz.sar7ar.springcore.model.pojo_models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public abstract class User {
    @Getter
    @Setter
    protected String firstName;
    @Getter
    @Setter
    protected String lastName;
    @Getter
    @Setter
    protected String username;
    @Getter
    @Setter
    protected String password;
    protected boolean isActive;

    protected User(String firstName, String lastName, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
