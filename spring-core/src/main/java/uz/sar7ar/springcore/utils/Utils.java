package uz.sar7ar.springcore.utils;

import uz.sar7ar.springcore.model.entities.dto.TraineeDto;
import uz.sar7ar.springcore.model.entities.dto.TrainerDto;
import uz.sar7ar.springcore.model.entities.dto.UserDto;

import java.security.SecureRandom;
import java.util.*;

public class Utils {
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();
    private static final Map<String, Integer> usernameAndSerialNumber = new HashMap<>();

    public static String generateUserName(String firstName, String lastName){
        String username = firstName+"."+lastName;
        if (usernameAndSerialNumber.get(username) == null){
            int serialNumber = 0;
            usernameAndSerialNumber.put(username, serialNumber);
            return username;
        } else {
            int nextSerialNUmber = usernameAndSerialNumber.get(username)+1;
            usernameAndSerialNumber.put(username, nextSerialNUmber);
            return username + nextSerialNUmber;
        }
    }

    public static String generateRandomPassword(){
        int PASSWORD_LENGTH = 10;
        StringBuilder builder = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++)
            builder.append(ALPHANUMERIC_CHARACTERS.charAt(
                                                    RANDOM.nextInt(
                                                            ALPHANUMERIC_CHARACTERS.length())));
        return builder.toString();
    }

    public <T> void leaveFieldsOnly(T dto, List<String> fields) {
        TraineeDto traineeDto = null;
        TrainerDto trainerDto = null;
        UserDto userDto = null;


        if (dto.getClass().isInstance(TraineeDto.class)) {
            traineeDto = (TraineeDto) dto;
            Arrays.stream(trainerDto
                            .getClass()
                            .getFields())
                            .forEach(field -> {field.setAccessible(true);
                                           if(!fields.contains(field.getName())) {
                                               try {field.set(field, null);}
                                               catch (IllegalAccessException e) {throw new RuntimeException(e);}
                                           }
            });
        }
        else if (dto.getClass().isInstance(TrainerDto.class)) trainerDto = (TrainerDto) dto;
        else if (dto.getClass().isInstance(UserDto.class)) userDto = (UserDto) dto;
    }
}
