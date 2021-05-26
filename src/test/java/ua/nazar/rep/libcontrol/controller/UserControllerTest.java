package ua.nazar.rep.libcontrol.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails("mike")
    void getProfileTest() throws Exception {
        this.mockMvc.perform(get("/user/profile"))
                .andDo(print())
                .andExpect(content().string(containsString("mike")))
                .andExpect(xpath("//*[@id='email-profile' and @value='u2@u2.u2']").exists());
    }

    @Test
    @WithUserDetails("testClient")
    void updateProfileTest() throws Exception {
        this.mockMvc.perform(
                post("/user/profile")
                        .param("email", "tes@tma.il")
                        .param("password", "")
                        .param("password2", "")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/user/profile"))
                .andDo(print())
                .andExpect(content().string(containsString("testClient")))
                .andExpect(xpath("//*[@id='email-profile' and @value='tes@tma.il']").exists());

    }

    @Test
    @WithUserDetails("testDirector")
    void userListTest() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(xpath("//*[@id='users-list']/tr")
                        .nodeCount(3))
                .andExpect(xpath("//*[@id='users-list']/tr[@data-id='1']/td[@data-type='username']")
                        .string("testLibrarian"))
                .andExpect(xpath("//*[@id='users-list']/tr[@data-id='4']/td[@data-type='username']")
                        .string("testManager"));
    }

    @Test
    @WithUserDetails("testDirector")
    void userEditFormTest() throws Exception {
        this.mockMvc.perform(get("/user/4"))
                .andDo(print())
                .andExpect(content().string(containsString("Changing data of the user:")))
                .andExpect(content().string(containsString("testManager")))
                .andExpect(content().string(containsString("ROLE_MANAGER")))
                .andExpect(xpath("//*[@id='email-profile' and @value='u4@u4.u4']").exists());
    }

    @Test
    @WithUserDetails("testDirector")
    void saveUserTest() throws Exception {
        this.mockMvc.perform(
                post("/users")
                        .param("userId", "1")
                        .param("email", "tes@tma.il")
                        .param("password", "")
                        .param("password2", "")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/users"));

        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(xpath("//*[@id='users-list']/tr[@data-id='1']/td[@data-type='email']")
                        .string("tes@tma.il"));

    }

    @Test
    @WithUserDetails("testLibrarian")
    void differentPasswordsValidationTest() throws Exception {
        this.mockMvc.perform(
                post("/user/profile")
                        .param("email", "tes@tma.il")
                        .param("password", "123")
                        .param("password2", "321")
                        .with(csrf()))
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(1))
                .andExpect(content().string(containsString("Passwords are different")));
    }

    @Test
    @WithUserDetails("testManager")
    void emailValidationTest() throws Exception {
        this.mockMvc.perform(
                post("/user/profile")
                        .param("email", "te")
                        .param("password", "123")
                        .param("password2", "123")
                        .with(csrf()))
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(1));

        this.mockMvc.perform(
                post("/user/profile")
                        .param("email", "")
                        .param("password", "123")
                        .param("password2", "123")
                        .with(csrf()))
                .andDo(print())
                .andExpect(xpath("//*[contains(@class, 'form-control is-invalid')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[contains(@class, 'invalid-feedback')]")
                        .nodeCount(1));

    }
}