package ua.nazar.rep.libcontrol.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ua.nazar.rep.libcontrol.controller.BookController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/catalog-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/catalog-after.sql", "/create-user-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {

    private final BookController controller;

    private final MockMvc mockMvc;

    @Autowired
    public BookControllerTest(BookController controller, MockMvc mockMvc) {
        this.controller = controller;
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails(value = "testClient")
    public void booksListTest() throws Exception {
        this.mockMvc.perform(get("/catalog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='books-list']/tr")
                        .nodeCount(5));
    }

    @Test
    @WithUserDetails(value = "testClient")
    public void filterBooksTest() throws Exception {
        this.mockMvc.perform(get("/catalog").param("filter", "0"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(
                        xpath("//*[@id='books-list']/tr")
                                .nodeCount(3))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='1']/td[@data-type='code']")
                                .exists())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='1']/td[@data-type='name']")
                                .exists());

        this.mockMvc.perform(get("/catalog").param("filter", "2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(
                        xpath("//*[@id='books-list']/tr")
                                .nodeCount(2));
        this.mockMvc.perform(get("/my-books").param("filter", "1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(
                        xpath("//*[@id='books-list']/tr")
                                .nodeCount(1));
    }

    @Test
    @WithUserDetails(value = "testLibrarian")
    public void addBookToListTest() throws Exception {
        this.mockMvc.perform(get("/catalog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='books-list']/tr").nodeCount(5));

        MockHttpServletRequestBuilder multipart = multipart("/catalog")
                .param("code", "1")
                .param("name", "2")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/catalog"));

        this.mockMvc.perform(get("/catalog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(
                        xpath("//*[@id='books-list']/tr")
                                .nodeCount(6))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='10']")
                                .exists())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='10']/td[@data-type='code']/a")
                                .string("1"))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='10']/td[@data-type='name']/a")
                                .string("2"));
    }
}
