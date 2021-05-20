package ua.nazar.rep.libcontrol;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class RegistrationTest {
    private final MockMvc mockMvc;

    @Autowired
    public RegistrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void registrationPageLoads() throws Exception {
        this.mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsStringIgnoringCase("Registration")));
    }

    @Test
    public void successfulRegistrationTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/registration")
                .param("username", "name")
                .param("password", "rewq")
                .param("password2", "rewq")
                .param("email", "e@e.e")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void notEmptyValidationTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/registration")
                .param("username", "")
                .param("password", "")
                .param("password2", "")
                .param("email", "")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                                .nodeCount(4))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(4))
                .andExpect(content().string(containsString("Username cannot be empty")))
                .andExpect(content().string(containsString("Password cannot be empty")))
                .andExpect(content().string(containsString("Password Confirmation cannot be empty")))
                .andExpect(content().string(containsString("Email cannot be empty")));

    }

    @Test
    public void differentPasswordsValidationTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/registration")
                .param("username", "qwera")
                .param("password", "asfs")
                .param("password2", "asdf")
                .param("email", "e@e.e")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(1))
                .andExpect(content().string(containsString("Passwords are different")));

    }

    @Test
    public void emailValidationTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/registration")
                .param("username", "qwera")
                .param("password", "rew")
                .param("password2", "rew")
                .param("email", "trew")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(1))
                .andExpect(content().string(containsString("Email is not correct")));
    }

    @Test
    @Sql(value = {"/create-user-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-user-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void usernameAlreadyExistTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/registration")
                .param("username", "testClient")
                .param("password", "rew")
                .param("password2", "rew")
                .param("email", "e@e.e")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(1))
                .andExpect(content().string(containsString("User exists!")));
    }

}
