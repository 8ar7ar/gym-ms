package uz.sar7ar.springcore.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.sar7ar.springcore.model.entities.User;
import uz.sar7ar.springcore.repository.jpa.UserRepository;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Slf4j
@AllArgsConstructor
@Component
public class UserUtils {
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    private final UserRepository userRepository;

    public String generateUserName(String firstName, String lastName){
        log.info("Generating username for [{} {}]", firstName, lastName);
        String initialUsername = firstName+"."+lastName;
        String finalUserName = initialUsername;

        int counter = 1;
        while (true){
            Optional<User> optUser = userRepository.findByUserName(finalUserName);
            if (optUser.isPresent()) finalUserName = initialUsername + counter++;
            else break;
        }
        return finalUserName;
    }

    public String generateRandomPassword(){
        log.info("Generating random password");
        int PASSWORD_LENGTH = 10;
        StringBuilder builder = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++)
            builder.append(ALPHANUMERIC_CHARACTERS.charAt(
                                                    RANDOM.nextInt(
                                                        ALPHANUMERIC_CHARACTERS.length())));
        return builder.toString();
    }

}
