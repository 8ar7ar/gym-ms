package uz.sar7ar.springcore.security.brute_force_protector;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 3;
    private final int LOCK_TIME_DURATION = 5;
    private Map<String, Integer> attemptCache = new HashMap<>();
    private Map<String, LocalDateTime> lockTimeCache = new HashMap<>();

    public void loginSucceeded(String key){
        attemptCache.remove(key);
        lockTimeCache.remove(key);
    }

    public void loginFailed(String key){
        int attemts = 0;
        if (attemptCache.containsKey(key)) attemts = attemptCache.get(key);
        attemts++;
        attemptCache.put(key, attemts);
        if (attemts >= MAX_ATTEMPT) lockTimeCache.put(key, LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
    }

    public boolean isBlocked(String key){
        if (!lockTimeCache.containsKey(key)) return false;
        LocalDateTime lockTime = lockTimeCache.get(key);
        if (LocalDateTime.now().isAfter(lockTime)){
            lockTimeCache.remove(key);
            attemptCache.remove(key);
            return false;
        }
        return true;
    }
    public int getRemainingLockTime(String key){
        if(lockTimeCache.containsKey(key))
            return (int) Duration.between(LocalDateTime.now(),
                                          lockTimeCache.get(key)).toMinutes();
        return 0;
    }
}
