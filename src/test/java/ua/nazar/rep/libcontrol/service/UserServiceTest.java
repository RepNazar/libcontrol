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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

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
                        anyString(),
                        anyString()
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
        Mockito.verify(passwordEncoder, Mockito.times(0)).encode(anyString());
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        anyString(),
                        anyString(),
                        anyString()
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


    @Test
    public void findUserTest(){
        User userMock = Mockito.mock(User.class);
        userService.findUser(userMock);
        Mockito.verify(userRepo, Mockito.times(1)).findById(userMock.getId());
    }

    @Test
    public void findAllNotClientTest(){
        userService.findAllNotClient();
        Mockito.verify(userRepo, Mockito.times(1)).findByRolesNotContains(Role.ROLE_CLIENT);
    }

    @Test
    public void loadUserByUsernameTest() {
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("123");
        userService.loadUserByUsername("123");
        Mockito.verify(userRepo, Mockito.times(1)).findByUsername("123");
    }

    @Test
    public void updateProfileNotChangedTest(){
        User userMock = mock(User.class);
        Mockito.doReturn("test@mail.com")
                .when(userMock)
                .getEmail();
        userService.updateProfile(userMock,"","test@mail.com");

        Mockito.verify(userMock, Mockito.times(0)).setEmail("test@mail.com");
        Mockito.verify(userMock, Mockito.times(0)).setActivationCode(anyString());

        Mockito.verify(userRepo, Mockito.times(1)).save(userMock);
        Mockito.verify(passwordEncoder, Mockito.times(0)).encode(anyString());
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.eq("test@mail.com"),
                        anyString(),
                        anyString()
                );
    }

    @Test
    public void updateProfilePasswordTest(){
        User userMock = mock(User.class);
        Mockito.doReturn("test@mail.com")
                .when(userMock)
                .getEmail();
        userService.updateProfile(userMock,"1234","test@mail.com");

        Mockito.verify(userMock, Mockito.times(0)).setEmail("test@mail.com");
        Mockito.verify(userMock, Mockito.times(0)).setActivationCode(anyString());

        Mockito.verify(userRepo, Mockito.times(1)).save(userMock);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("1234");
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq("test@mail.com"),
                        ArgumentMatchers.eq("Data Changed"),
                        anyString()
                );
    }

    @Test
    public void updateProfileEmailTest(){
        User userMock = mock(User.class);
        Mockito.doReturn("test@mail.com")
                .when(userMock)
                .getEmail();
        userService.updateProfile(userMock,"","new.test@mail.com");

        Mockito.verify(userMock, Mockito.times(1)).setEmail("new.test@mail.com");
        Mockito.verify(userMock, Mockito.times(1)).setActivationCode(anyString());
        Mockito.verify(userMock, Mockito.times(0)).setPassword(anyString());

        Mockito.verify(userRepo, Mockito.times(1)).save(userMock);
        Mockito.doReturn("new.test@mail.com")
                .when(userMock)
                .getEmail();

        Mockito.verify(passwordEncoder, Mockito.times(0)).encode(anyString());
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        anyString(),
                        ArgumentMatchers.eq("Activation code"),
                        anyString()
                );
    }

    @Test
    public void updateProfileEmailAndPasswordTest(){
        User userMock = mock(User.class);
        Mockito.doReturn("test@mail.com")
                .when(userMock)
                .getEmail();
        userService.updateProfile(userMock,"1234","new.test@mail.com");

        Mockito.verify(userMock, Mockito.times(1)).setEmail("new.test@mail.com");
        Mockito.verify(userMock, Mockito.times(1)).setActivationCode(anyString());

        Mockito.verify(userRepo, Mockito.times(1)).save(userMock);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("1234");
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        anyString(),
                        ArgumentMatchers.eq("Activation code and credentials"),
                        anyString()
                );
    }

}