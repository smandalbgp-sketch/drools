package com.vd.drools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProvidentFundRulesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String body(String department, Integer experience) throws Exception {
        Map<String, Object> m = new HashMap<>();
        m.put("department", department);
        m.put("experience", experience);
        return objectMapper.writeValueAsString(m);
    }

    private void expectCut(int experience, String expected) throws Exception {
        mockMvc.perform(post("/provident-cut-general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("it", experience)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("200"))
                .andExpect(jsonPath("$.response.department").value("IT"))
                .andExpect(jsonPath("$.response.providentCut").value(expected));
    }

    @Test
    @WithMockUser(roles = "user")
    void zeroToTwoYears() throws Exception {
        expectCut(0, "5% of basic salary");
        expectCut(2, "5% of basic salary");
    }

    @Test
    @WithMockUser(roles = "user")
    void threeToFourYears() throws Exception {
        expectCut(3, "6% of basic salary");
        expectCut(4, "6% of basic salary");
    }

    @Test
    @WithMockUser(roles = "user")
    void fiveToSixYears() throws Exception {
        expectCut(5, "7% of gross salary");
        expectCut(6, "7% of gross salary");
    }

    @Test
    @WithMockUser(roles = "user")
    void moreThanSixYears() throws Exception {
        expectCut(7, "9% of gross salary");
        expectCut(15, "9% of gross salary");
    }

    @Test
    void unauthenticatedIsRejected() throws Exception {
        mockMvc.perform(post("/provident-cut-general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("it", 3)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void userCannotCallAdminEndpoint() throws Exception {
        mockMvc.perform(post("/provident-cut-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("it", 3)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "admin")
    void adminCanCallAdminEndpoint() throws Exception {
        mockMvc.perform(post("/provident-cut-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("it", 3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.providentCut").value("6% of basic salary"));
    }

    @Test
    @WithMockUser(roles = "user")
    void blankDepartmentIsBadRequest() throws Exception {
        mockMvc.perform(post("/provident-cut-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("", 3)))
                .andExpect(status().isBadRequest());
    }
}
