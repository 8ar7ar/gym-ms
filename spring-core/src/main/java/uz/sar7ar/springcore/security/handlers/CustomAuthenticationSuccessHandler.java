package uz.sar7ar.springcore.security.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.sar7ar.springcore.security.brute_force_protector.LoginAttemptService;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final LoginAttemptService loginAttemptService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                 throws IOException{
        String username = request.getParameter("username");
        if(username != null) loginAttemptService.loginSucceeded("username");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Login successful");
    }
}
