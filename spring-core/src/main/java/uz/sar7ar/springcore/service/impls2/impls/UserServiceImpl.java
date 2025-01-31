package uz.sar7ar.springcore.service.impls2.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.exceptions.InvalidUserPasswordException;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.exceptions.UserPasswordConfirmationException;
import uz.sar7ar.springcore.model.entities.User;
import uz.sar7ar.springcore.model.entities.dto.UserDto;
import uz.sar7ar.springcore.repository.jpa.UserRepository;
import uz.sar7ar.springcore.service.impls2.UserService;
import uz.sar7ar.springcore.utils.UserUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserUtils userUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserDto newUserDto) {
        log.info("Creating new User");
        User newUser = new User();
        newUser.setFirstName(newUserDto.getFirstName()== null ? "" : newUserDto.getFirstName());
        newUser.setLastName(newUserDto.getLastName() == null ? "" : newUserDto.getLastName());
        newUser.setRole(newUserDto.getRole());
        newUser.setUserName(userUtils.generateUserName(newUserDto.getFirstName(),
                                                       newUserDto.getLastName()));
        String generatedPassword = userUtils.generateRandomPassword();
        newUser.setPassword(generatedPassword);

        if(newUserDto.getIsActive() != null) newUser.setIsActive(newUserDto.getIsActive());
        else newUser.setIsActive(false);

        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(UserDto updatedUserDto) {
        log.info("Updating Trainer");
        User updatedUser = userRepository.findByUserName(updatedUserDto.getUserName()).get();
        if (!Objects.equals(updatedUser.getFirstName(), updatedUserDto.getFirstName())
                || !Objects.equals(updatedUser.getLastName(), updatedUserDto.getLastName())) {
            updatedUser.setFirstName(updatedUserDto.getFirstName());
            updatedUser.setLastName(updatedUserDto.getLastName());
            updatedUser.setUserName(userUtils.generateUserName(updatedUserDto.getFirstName(),
                                                           updatedUserDto.getLastName()));
        }
        updatedUser.setIsActive(updatedUserDto.getIsActive());
        log.info("Trainer updated and persisted");

        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByUserName(String userName) {
        log.info("Retrieving userDto profile");
        Optional<User> user = userRepository.findByUserName(userName);
        log.info("User profile returned");

        return user.map(User::toUserDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean authenticateUser(String userName, String password)
                             throws UserNameNotFoundException,
                                    InvalidUserPasswordException {
        log.info("Authenticating userDto profile");
        Optional<User> optUser = userRepository.findByUserName(userName);
        if (optUser.isEmpty()) {
            log.warn("No User profile with name: [{}]", userName);
            throw new UserNameNotFoundException(
                            "<< " + userName + " >> not found. Check your spelling please.");
        } else {
            log.info("Found user profile with name: [{}]", userName);
            if(optUser.get().getPassword().equals(passwordEncoder.encode(password))) return true;
            else throw new InvalidUserPasswordException("Invalid password was provided for << " +
                                                        userName + " >>. Check your spelling please.");
        }
    }

    @Override
    public boolean changeUsersPassword(String userName, String oldPassword,
                                       String newPassword, String newPasswordConfirm)
                                throws UserPasswordConfirmationException,
                                       UserNameNotFoundException,
                                       InvalidUserPasswordException {
        log.info("User [{}] requested password change", userName);
//        authenticateUser(userName, passwordEncoder.encode(oldPassword));

        String username = ((UserDetails) SecurityContextHolder
                                                .getContext()
                                                .getAuthentication()
                                                .getPrincipal())
                           .getUsername();

        User user = userRepository.findByUserName(username)
                               .orElseThrow(()-> new UserNameNotFoundException("Username not found"));
        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new IllegalArgumentException("Old password is incorrect");
        if(!newPassword.equals(newPasswordConfirm))
            throw  new UserPasswordConfirmationException( "Provided password and confirm_password do not match. " +
                                                          "Please double check your spelling in both cases.");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;


//
//        if (newPassword.equals(newPasswordConfirm)) {
//            String newHashedPassword = passwordEncoder.encode(newPassword);
//            userRepository.findByUserName(userName).get().setPassword(newHashedPassword);
//            userRepository.flush();
//            log.info("User [{}] changed his/her password", userName);
//            return true;
//        } else {
//            log.warn("User [{}] provided password and confirm_password mismatched", userName);
//            throw new UserPasswordConfirmationException(
//                    "Provided password and confirm_password do not match. " +
//                            "Please double check your spelling in both cases.");
//        }
    }

    @Override
    public void activateUser(String userName) {
        User user = userRepository.findByUserName(userName).get();
        user.setIsActive(true);
        userRepository.save(user);
        log.info("User [{}] is activated", userName);
    }

    @Override
    public void deActivateUser(String userName) {
        User user = userRepository.findByUserName(userName).get();
        user.setIsActive(false);
        userRepository.save(user);
        log.info("User [{}] is deActivated", userName);
    }
}
