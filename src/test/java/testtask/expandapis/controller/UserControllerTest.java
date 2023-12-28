package testtask.expandapis.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import testtask.expandapis.dto.UserAuthDto;
import testtask.expandapis.dto.UserLoginResponseDto;
import testtask.expandapis.dto.UserResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    protected static MockMvc mockMvc;
    private static UserAuthDto mainRequestDto;
    private static UserResponseDto mainResponseDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUpBeforeClass(@Autowired DataSource data,
            @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(appContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        tearDown(data);
        try (Connection connection = data.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/db/test_queries/add-user.sql"));
        }
        mainRequestDto = new UserAuthDto("testUser", "testPassword");
        //mainResponseDto = new UserResponseDto(1L, "testUser");
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
                    new ClassPathResource("/db/test_queries/delete-all-users.sql"));
        }
    }

    @Sql(value = "classpath:db/test_queries/delete-added-user.sql", 
            executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Register a new user")
    void register_NewUsernameInDb_Ok() throws Exception {
        UserAuthDto requestDto = new UserAuthDto("testUser2", "testPassword");
        UserResponseDto responseDto = new UserResponseDto(2L, "testUser2");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        UserResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                UserResponseDto.class);
        assertNotNull(actual);
        assertEquals(responseDto.username(), actual.username());
        assertEquals(responseDto.id(), actual.id());
    }
    
    @Test
    @DisplayName("Login user")
    void login_RegistredUser_Ok() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(mainRequestDto);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/authenticate")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        UserLoginResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserLoginResponseDto.class);
        assertNotNull(actual);
    }
}
