package uz.sar7ar.springcore.security.brute_force_protector;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class BruteForceProtectionFilter extends OncePerRequestFilter {
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationFailureHandler failureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                             throws ServletException, IOException {
        String username = request.getParameter("username");

        if (username != null && loginAttemptService.isBlocked(username)) {
            int remainingLockTime = loginAttemptService.getRemainingLockTime(username);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("User is locked due to too many failed login attempts. " +
                                         "Try again in " + remainingLockTime + " minutes.");
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(username);
            failureHandler.onAuthenticationFailure(request, response, e);
        }
    }
}
