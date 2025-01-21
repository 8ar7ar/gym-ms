package uz.sar7ar.springcore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import uz.sar7ar.springcore.exceptions.InvalidUserPasswordException;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.exceptions.UserPasswordConfirmationException;
import uz.sar7ar.springcore.model.entities.Roles;
import uz.sar7ar.springcore.model.entities.TrainingTypeEnum;
import uz.sar7ar.springcore.model.entities.dto.*;
import uz.sar7ar.springcore.security.jwt.JwtBlacklistService;
import uz.sar7ar.springcore.security.jwt.JwtUtil;
import uz.sar7ar.springcore.security.user_details.CustomUserDetailsService;
import uz.sar7ar.springcore.service.impls2.TraineeService;
import uz.sar7ar.springcore.service.impls2.TrainerService;
import uz.sar7ar.springcore.service.impls2.UserService;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @PostMapping("/trainee-registration")
    @Operation(summary = "Trainee Registration")
    public ResponseEntity<UserCredentialsDto> createNewTraineeProfile(@RequestParam(value = "first-name") String firstName,
                                                                     @RequestParam(value = "last-name") String lastName,
                                                                     @Parameter(description = "format: yyyy-mm-dd")
                                                                     @RequestParam(value = "date-of-birth", required = false)
                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                                                     @RequestParam(value = "address", required = false) String address) {
        TraineeDto newTraineeDto = new TraineeDto(dateOfBirth, address, new UserDto(firstName, lastName, Roles.TRAINEE));
        TraineeDto createdTrainee = traineeService.createTrainee(newTraineeDto);
        UserCredentialsDto userCredentials = new UserCredentialsDto(createdTrainee.getUser().getUserName(),
                                                                    createdTrainee.getUser().getPassword());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userCredentials);
    }

    @PostMapping("/trainer-registration")
    @Operation(summary = "Trainer Registration")
    public ResponseEntity<UserCredentialsDto> createNewTrainerProfile(
            @RequestParam(value = "first-name") String firstName,
            @RequestParam(value = "last-name") String lastName,
            @RequestParam(value = "specialization") TrainingTypeEnum specialization) {
        TrainerDto newTrainerDto = new TrainerDto(new TrainingTypeDto(specialization),
                new UserDto(firstName, lastName, Roles.TRAINER));
        TrainerDto createdTrainer = trainerService.createTrainer(newTrainerDto);
        UserCredentialsDto userCredentials = new UserCredentialsDto(createdTrainer.getUser().getUserName(),
                createdTrainer.getUser().getPassword());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userCredentials);
    }

    @GetMapping("/login")
    @Operation(summary = "Login to trainee/trainer profile")
    public ResponseEntity<String> authenticateUser(@RequestParam("username") String userName,
                                                   @RequestParam("password") String password){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, password));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            final String jwt = jwtUtil.generateToken(userDetails);

            StringBuilder authorities = new StringBuilder();
            authorities.append("{ ");
            authentication.getAuthorities().forEach(authority -> authorities.append(authority.getAuthority()).append(", "));
            authorities.delete(authorities.length() - 2, authorities.length() - 1);
            authorities.append("} ");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authorities + "<< " + userName + " >> successfully authenticated.\n" +
                          "Your jwt: " + jwt);
        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid username and password.");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out from trainee/trainer profile")
    public ResponseEntity<String> logOut(HttpServletRequest request,
                                         HttpServletResponse response){
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer")){
            token = token.substring(7);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null && auth.isAuthenticated()){
                new SecurityContextLogoutHandler().logout(request, response, auth);
                jwtBlacklistService.blacklistToken(token);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("You have successfully logged out and your token invalidated.");
            }
        }
        return  ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized: No valid token or user session");
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change Login (password) trainee/trainer profile")
    public ResponseEntity<String> updateUserPassword(@RequestParam("username") String userName,
                                                     @RequestParam("old-password") String oldPassword,
                                                     @Parameter(description = "password length must be exactly 10 symbols")
                                                     @RequestParam("new-password") String newPassword,
                                                     @RequestParam("new-password-confirmation") String newPasswordConfirmation)
                                              throws UserNameNotFoundException,
                                                     InvalidUserPasswordException,
                                                     UserPasswordConfirmationException {
        userService.changeUsersPassword(userName, oldPassword, newPassword, newPasswordConfirmation);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userName + "'s password successfully updated.");
    }
}
