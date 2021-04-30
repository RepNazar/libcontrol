package ua.nazar.rep.libcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.UserRepo;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Value("${hostname}")
    private String hostname;

    public List<User> findAllNotClient() {
        return userRepo.findByRolesNotContains(Role.ROLE_CLIENT);
    }

    public User findUser(User user) {
        return userRepo.findById(user.getId()).orElse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user, Role role) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setRoles(Collections.singleton(role));
        user.setActive(true);
        user.setActivationCode(UUID.randomUUID().toString());
        sendFullMessage(user, user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);
        return true;
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged =
                ((email != null && !email.equals(userEmail)) ||
                        (userEmail != null && !userEmail.equals(email))
                ) && !StringUtils.isEmpty(email);

        if (isEmailChanged) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);
        if (isEmailChanged) {
            if (!StringUtils.isEmpty(password)) {
                sendFullMessage(user, password);
            } else {
                sendActivationMessage(user);
            }
        } else if (!StringUtils.isEmpty(password)) {
            sendDataMessage(user, user.getUsername(), password);
        }
    }

    private void sendActivationMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n\n" +
                            "Welcome to our Library.\n\n" +
                            "Please, visit next link for your account activation: \n" +
                            "http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    private void sendDataMessage(User user, String oldUsername, String password) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n\n" +
                            "Your account data was changed:\n\n" +
                            "Username:\n%s\n" +
                            "Password:\n%s\n",
                    oldUsername,
                    user.getUsername(),
                    password
            );

            mailSender.send(user.getEmail(), "Data Changed", message);
        }
    }

    private void sendFullMessage(User user, String password) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n\n" +
                            "Welcome to our Library team.\n" +
                            "Your account data is:\n\n" +
                            "Username:\n%s\n" +
                            "Password:\n%s\n\n" +
                            "Please, visit next link for your account activation: \n" +
                            "http://%s/activate/%s",
                    user.getUsername(),
                    user.getUsername(),
                    password,
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActive(true);
        user.setActivationCode(null);

        userRepo.save(user);

        return true;
    }

}
