package uz.sar7ar.springcore.security.raw_user_password_hashing_utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.model.entities.User;
import uz.sar7ar.springcore.repository.jpa.UserRepository;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class PasswordEncryptionRunner implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args){
        log.info("Hashing raw password in DB...");
        List<User> users = userRepository.findAll();
        for (User user : users)
            if (!isPasswordHashed(user.getPassword())) {
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
            }
        log.info("Hashing completed");
    }

    private boolean isPasswordHashed(String password) {
        return password != null && password.startsWith("$2a$");
    }
}
