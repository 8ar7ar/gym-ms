package uz.sar7ar.springcore.security.user_details;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import uz.sar7ar.springcore.security.brute_force_protector.LoginAttemptService;
import uz.sar7ar.springcore.security.jwt.JwtUtil;

import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private LoginAttemptService loginAttemptService;
    private JwtUtil jwtUtil;

    @Autowired
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      LoginAttemptService loginAttemptService,
                                      JwtUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.loginAttemptService = loginAttemptService;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/v1/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
                                         throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (loginAttemptService.isBlocked(username))
            throw new RuntimeException("User is locked due to too many failed login attempts. Try again later.");
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
                                     throws IOException{
        String username = authResult.getName();
        loginAttemptService.loginSucceeded(username);
        String token = jwtUtil.generateToken((UserDetails) authResult.getPrincipal());
        response.setHeader("Authorization", "Bearer " + token);
        response.getWriter().write("Login successful!");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
                                       throws IOException{
        String username = request.getParameter("username");
        loginAttemptService.loginFailed(username);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Login failed: " + failed.getMessage());
    }
}
