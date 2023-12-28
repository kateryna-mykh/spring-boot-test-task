package testtask.expandapis.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import testtask.expandapis.dto.NewProductsDto;
import testtask.expandapis.dto.ProductDto;
import testtask.expandapis.dto.ProductResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUpBeforeClass(@Autowired DataSource data,
            @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(appContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        tearDown(data);
    }

    @AfterAll
    static void tearDownAfterClass(@Autowired DataSource data) throws Exception {
        tearDown(data);
    }

    @SneakyThrows
    static void tearDown(DataSource data) {
        try (Connection connection = data.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/db/test_queries/delete-all-products.sql"));
        }
    }

    @Sql(scripts = "classpath:db/test_queries/delete-all-products.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Add two products to DB")
    @WithMockUser(username = "testUser", authorities = "USER")
    void addProducts_ValidBody_ReturnListOfDtos() throws Exception {
        ProductDto firstAddedProduct = new ProductDto("01-03-2023", "1123", "Item 1", 10L, "New");
        ProductDto secondAddedProduct = new ProductDto("01-03-2023", "1124", "Item 2", 1L, "Paid");
        List<ProductResponseDto> expectedList = List.of(
                new ProductResponseDto(1L, "2023-03-01", "1123", "Item 1", 10L, "New"),
                new ProductResponseDto(2L, "2023-03-01", "1124", "Item 2", 1L, "Paid"));
        NewProductsDto requestDto = new NewProductsDto("products",
                List.of(firstAddedProduct, secondAddedProduct));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/products/add").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        ProductResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ProductResponseDto[].class);
        assertNotNull(actual);
        assertEquals(2, actual.length);
        assertEquals(expectedList, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Get empty list of product Dtos")
    @WithMockUser(username = "testUser", authorities = "USER")
    void getAll_EmptyDb_ReturnEmptyList() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString().isEmpty());
    }
    
    @Sql(scripts = "classpath:db/test_queries/add-products.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/test_queries/delete-all-products.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Get all products from DB")
    @WithMockUser(username = "testUser", authorities = "USER")
    void getAll_ThreeProductsInDb_ReturnListOfDtos() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ProductResponseDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                ProductResponseDto[].class);
        assertEquals(3, actual.length);
    }
}
