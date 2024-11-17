package uz.sar7ar.springcore.service.impls2.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uz.sar7ar.springcore.exceptions.InvalidUserPasswordException;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.exceptions.UserPasswordConfirmationException;
import uz.sar7ar.springcore.model.entities.dto.UserDto;
import uz.sar7ar.springcore.service.impls2.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class o1_UserServiceTest {
    private final UserService userService;

    @Autowired
    o1_UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    void getUserByUserName() {
        //('Roseanna', 'Bourhill', 'Roseanna.Bourhill', 'xq72p1Z142', true);

        //when correct user_name then successfully returns userDto entity
        Optional<UserDto> optUser = userService.getUserByUserName("Roseanna.Bourhill");
        assertTrue(optUser.isPresent());
        UserDto user = optUser.get();
        assertNotNull(user);
        assertEquals("Roseanna.Bourhill", user.getUserName());

        //when invalid user_name then returns empty optional object
        Optional<UserDto> optUser2 = userService.getUserByUserName("Fake.Username");
        assertTrue(optUser2.isEmpty());
        assertFalse(optUser2.isPresent());
        assertThrows(NoSuchElementException.class, optUser2::get);
    }

    @Test
    void authenticateUser() {
        // ('Auberta', 'Elflain', 'Auberta.Elflain', 'RY87Nlj678', true);
        try {
            // when correct username and password, then returns true
            assertTrue(userService.authenticateUser("Auberta.Elflain", "RY87Nlj678"));

            // when correct userName but invalid password, then returns false
            assertThrows(InvalidUserPasswordException.class,
                    ()-> userService.authenticateUser("Auberta.Elflain", "Auberta.Elflain"));
//            assertFalse(userService.authenticateUser("Auberta.Elflain", "Auberta.Elflain"));

            // when invalid username, then throws exception
            assertThrows(UserNameNotFoundException.class,
                    () -> userService.authenticateUser("fake_username", "RY87Nlj678"));
        } catch (UserNameNotFoundException | InvalidUserPasswordException e) {
            assertEquals(UserNameNotFoundException.class, e.getClass());
        }
    }

    @Test
    void changeUsersPassword() {
        // ('Shirleen', 'Bysouth', 'Shirleen.Bysouth', 'tn23LNn096', true);
        try {
            // when correct password and passwordConfirmation, then returns true
            assertTrue(userService.changeUsersPassword(
                    "Shirleen.Bysouth",
                    "tn23LNn096",
                    "1234567890",
                    "1234567890"));

            // when incorrect passwordConfirmation, then throws exception
            assertThrows(UserPasswordConfirmationException.class,
                    () -> userService.changeUsersPassword(
                            "Shirleen.Bysouth",
                            "1234567890",
                            "1234567890",
                            "0234567891"));
        } catch (UserPasswordConfirmationException | UserNameNotFoundException | InvalidUserPasswordException e) {
            assertEquals(UserPasswordConfirmationException.class, e.getClass());
        }
    }

    @Test
    void activateUser() {
        // ('Link', 'Brewin', 'Link.Brewin', 'N111IKj058', false);
        assertFalse(userService.getUserByUserName("Link.Brewin").get().getIsActive());
        userService.activateUser("Link.Brewin");
        assertTrue(userService.getUserByUserName("Link.Brewin").get().getIsActive());
    }

    @Test
    void deActivateUser() {
        // ('Allen', 'Butteris', 'Allen.Butteris', 'UY73Iz3858', true);
        assertTrue(userService.getUserByUserName("Allen.Butteris").get().getIsActive());
        userService.deActivateUser("Allen.Butteris");
        assertFalse(userService.getUserByUserName("Allen.Butteris").get().getIsActive());
    }
}