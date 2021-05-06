package ua.nazar.rep.libcontrol.service;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.UserRepo;

import java.util.Collections;

@SpringBootTest
public class UserServiceTest {
    private final UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void addUser() {
        User user = new User();

        user.setEmail("some@mail.ru");
        user.setPassword("123");

        boolean isUserCreated = userService.addUser(user, Role.ROLE_CLIENT);

        Assertions.assertTrue(isUserCreated);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.ROLE_CLIENT)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("123");
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();

        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user, Role.ROLE_CLIENT);

        Assertions.assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(passwordEncoder, Mockito.times(0)).encode(ArgumentMatchers.anyString());
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void activateUser() {
        User user = new User();

        user.setActivationCode("bingo!");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assertions.assertTrue(isUserActivated);
        Assertions.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate me");

        Assertions.assertFalse(isUserActivated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}