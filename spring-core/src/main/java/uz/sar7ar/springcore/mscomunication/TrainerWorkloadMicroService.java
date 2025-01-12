package uz.sar7ar.springcore.mscomunication;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "trainer-workload", url = "http://localhost:8082")
public interface TrainerWorkloadMicroService {


    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "defaultResponse")
    @PostMapping("/api/v1/trainer-workload-service/update-training")
    void updateTrainerWorkload(@RequestParam String trainerUsername,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam boolean isActive,
                               @RequestParam LocalDate trainingDate,
                               @RequestParam int durationInHours,
                               @RequestParam String actionType);

    default void defaultResponse(String trainerUsername,
                                 String firstName,
                                 String lastName,
                                 boolean isActive,
                                 LocalDate trainingDate,
                                 int duration,
                                 String actionType,
                                 Throwable t) {
        System.out.println("Fallback: Trainer workload service is unavailable.");
        System.out.println("Error occurred: " + t.getMessage());
        System.out.println("Failed request: Trainer - " + trainerUsername + " | Date: " + trainingDate + " | Action: " + actionType);
    }
}
