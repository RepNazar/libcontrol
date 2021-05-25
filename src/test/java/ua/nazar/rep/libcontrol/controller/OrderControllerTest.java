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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/catalog-before.sql", "/orders-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/orders-after.sql", "/catalog-after.sql", "/create-user-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    OrderControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails("testManager")
    void getAllOrdersTest() throws Exception {
        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(xpath("//*[@id='orders-list']/tr").nodeCount(6))
                .andExpect(xpath("//*[@id='orders-list']/tr[@data-id='5']/td[@data-type='book-code']/span")
                        .string("01"));
    }

    @Test
    @WithUserDetails("testManager")
    void getOrdersByUserTest() throws Exception {
        this.mockMvc.perform(get("/orders/3"))
                .andDo(print())
                .andExpect(xpath("//*[@id='orders-list']/tr").nodeCount(4))
                .andExpect(xpath("//*[@id='orders-list']/tr[@data-id='2']")
                        .exists());

        this.mockMvc.perform(get("/orders/2"))
                .andDo(print())
                .andExpect(xpath("//*[@id='orders-list']/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='orders-list']/tr[@data-id='1']")
                        .exists());
    }

    @Test
    @WithUserDetails("testClient")
    void getClientOrdersTest() throws Exception {
        this.mockMvc.perform(get("/my-orders"))
                .andDo(print())
                .andExpect(xpath("//*[@id='orders-list']/tr").nodeCount(4))
                .andExpect(xpath("//*[@id='orders-list']/tr[@data-id='2']")
                        .exists());
    }

    @Test
    @WithUserDetails("testManager")
    void getAllReturnRequestsTest() throws Exception {
        this.mockMvc.perform(get("/return-requests"))
                .andDo(print())
                .andExpect(xpath("//*[@id='requests-list']/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='requests-list']/tr[@data-id='8']/td[@data-type='book-code']/span")
                        .string("241"));
    }

    @Test
    @WithUserDetails("testManager")
    void getReturnRequestsByUserTest() throws Exception {
        this.mockMvc.perform(get("/return-requests/3"))
                .andDo(print())
                .andExpect(xpath("//*[@id='requests-list']/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='requests-list']/tr[@data-id='8']")
                        .exists());
    }

    @Test
    @WithUserDetails("testClient")
    void getClientReturnRequestsTest() throws Exception {
        this.mockMvc.perform(get("/my-requests"))
                .andDo(print())
                .andExpect(xpath("//*[@id='requests-list']/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='requests-list']/tr[@data-id='8']")
                        .exists());
    }

    @Test
    @WithUserDetails("testManager")
    void displayFilterOrdersTest() throws Exception {
        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(xpath("//*[@id='orders-list']/tr").nodeCount(6))
                .andExpect(xpath("//*[@id='orders-list']/tr[contains(@class, 'bg-warning')]")
                        .nodeCount(2))
                .andExpect(xpath("//*[@id='orders-list']/tr[contains(@class, 'bg-secondary')]")
                        .nodeCount(3))
                .andExpect(xpath("//*[@id='orders-list']/tr[contains(@class, 'bg-success')]")
                        .nodeCount(1));
    }

    @Test
    @WithUserDetails("testManager")
    void displayFilterRequestsTest() throws Exception {
        this.mockMvc.perform(get("/return-requests"))
                .andDo(print())
                .andExpect(xpath("//*[@id='requests-list']/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='requests-list']/tr[contains(@class, 'bg-warning')]")
                        .nodeCount(1))
                .andExpect(xpath("//*[@id='requests-list']/tr[contains(@class, 'bg-secondary')]")
                        .nodeCount(1));
    }

    @Test
    @WithUserDetails(value = "testClient")
    void commitOrderTest() throws Exception {
        this.mockMvc.perform(get("/my-orders"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(
                        xpath("//*[@id='orders-list']/tr").nodeCount(4));


        MockHttpServletRequestBuilder multipart = multipart("/order/2")
                .param("book", "2")
                .param("client", "3")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(redirectedUrl("/my-orders"));

        this.mockMvc.perform(get("/my-orders"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='orders-list']/tr").nodeCount(5))
                .andExpect(
                        xpath("//*[@id='orders-list']/tr[@data-id='10']/td[@data-type='status']")
                                .exists());
    }

    @Test
    @WithUserDetails("testManager")
    void approveOrder() throws Exception {
        this.mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(
                        xpath("//*[@id='orders-list']/tr[contains(@class, 'bg-warning')]")
                                .nodeCount(2))
                .andExpect(
                        xpath("//*[@id='orders-list']/tr[@data-id='5' and contains(@class, 'bg-warning')]")
                                .exists());

        this.mockMvc.perform(post("/orders")
                .param("order_id", "5")
                .with(csrf()));

        this.mockMvc.perform(get("/orders"))
                .andExpect(
                        xpath("//*[@id='orders-list']/tr[@data-id='5' and contains(@class, 'bg-success')]")
                                .exists());
    }

    @Test
    @WithUserDetails("testClient")
    void confirmOrderTest() throws Exception {
        this.mockMvc.perform(get("/my-books"))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='2']")
                                .doesNotExist());

        this.mockMvc.perform(get("/my-orders"))
                .andDo(print())
                .andExpect(
                        xpath("//*[@id='orders-list']/tr[@data-id='6' and contains(@class, 'bg-success')]")
                                .exists());

        this.mockMvc.perform(post("/my-orders")
                .param("order_id", "6")
                .with(csrf()));

        this.mockMvc.perform(get("/my-orders"))
                .andExpect(
                        xpath("//*[@id='orders-list']/tr[@data-id='6' and contains(@class, 'bg-secondary')]")
                                .exists());

        this.mockMvc.perform(get("/my-books"))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='2']")
                                .exists())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='2']/td[@data-type='code']/a")
                                .string("02"));
    }

    @Test
    @WithUserDetails(value = "testClient")
    void commitRequestTest() throws Exception {

        this.mockMvc.perform(get("/my-requests"))
                .andExpect(
                        xpath("//*[@id='requests-list']/tr[@data-id='10']/td[@data-type='status']")
                                .doesNotExist());

        this.mockMvc.perform(get("/my-books"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(
                        xpath("//*[@id='books-list']/tr").nodeCount(2))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='4']/td[@data-type='code']")
                                .exists())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='4']/td[@data-type='name']")
                                .exists());

        MockHttpServletRequestBuilder multipart = multipart("/my-books")
                .param("book", "4")
                .param("client", "3")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(redirectedUrl("/my-requests"));

        this.mockMvc.perform(get("/my-requests"))
                .andExpect(
                        xpath("//*[@id='requests-list']/tr[@data-id='10']/td[@data-type='status']")
                                .exists());

        this.mockMvc.perform(get("/my-books"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='books-list']/tr").nodeCount(1))
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='4']/td[@data-type='code']")
                                .doesNotExist())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='4']/td[@data-type='name']")
                                .doesNotExist());
    }

    @Test
    @WithUserDetails("testManager")
    void approveReturnRequest() throws Exception {
        this.mockMvc.perform(get("/catalog"))
                .andDo(print())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='5' and contains(@class, 'bg-secondary')]")
                                .exists());

        this.mockMvc.perform(get("/return-requests"))
                .andDo(print())
                .andExpect(
                        xpath("//*[@id='requests-list']/tr[@data-id='8' and contains(@class, 'bg-warning')]")
                                .exists());

        this.mockMvc.perform(post("/return-requests")
                .param("order_id", "8")
                .with(csrf()));

        this.mockMvc.perform(get("/return-requests"))
                .andDo(print())
                .andExpect(
                        xpath("//*[@id='requests-list']/tr[@data-id='8' and contains(@class, 'bg-secondary')]")
                                .exists());

        this.mockMvc.perform(get("/catalog"))
                .andDo(print())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='5' and contains(@class, 'bg-secondary')]")
                                .doesNotExist())
                .andExpect(
                        xpath("//*[@id='books-list']/tr[@data-id='5' and not(contains(@class, 'bg-secondary'))]")
                                .exists());
    }

}