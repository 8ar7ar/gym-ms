package uz.sar7ar.springcore.service.impls2;

import uz.sar7ar.springcore.exceptions.InvalidUserPasswordException;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.exceptions.UserPasswordConfirmationException;
import uz.sar7ar.springcore.model.entities.User;
import uz.sar7ar.springcore.model.entities.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    User createUser(UserDto newUserDto);

    User updateUser(UserDto updatedUserDto);

    Optional<UserDto> getUserByUserName(String userName);

    boolean authenticateUser(String userName,
                             String password)
                      throws UserNameNotFoundException,
                             InvalidUserPasswordException;

    boolean changeUsersPassword(String userName,
                                String oldPassword,
                                String newPassword,
                                String newPasswordConfirm)
                         throws UserPasswordConfirmationException,
                                UserNameNotFoundException,
                                InvalidUserPasswordException;

    void activateUser(String userName);

    void deActivateUser(String userName);
}
