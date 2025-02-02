package GeoexploreTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import Geoexplore.GeoExploreApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GeoExploreApplication.class)
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    private String validUserJson;
    private String invalidUserJson;

    @BeforeEach
    public void setUp() {
        validUserJson = """
            {
                "nome": "Mario",
                "cognome": "Rossi",
                "email": "mario.rossi@example.com",
                "username": "mario.rossi",
                "password": "securePass123"
            }
        """;

        invalidUserJson = """
            {
                "nome": "",
                "cognome": "",
                "email": "",
                "username": "",
                "password": ""
            }
        """;
    }

    @Test
    @Disabled
    public void testRegisterUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isCreated());
    }

    @Test
    @Disabled
    public void testRegisterUserWithMissingFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled
    public void testLoginUser() throws Exception {
        String loginJson = """
            {
                "username": "mario.rossi",
                "password": "securePass123"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    public void testLoginWithWrongPassword() throws Exception {
        String loginJson = """
            {
                "username": "mario.rossi",
                "password": "wrongPassword"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Disabled
    public void testAccessProtectedResourceWithoutAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/protected"))
                .andExpect(status().isForbidden());
    }
}
