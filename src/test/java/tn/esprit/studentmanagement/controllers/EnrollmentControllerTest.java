package tn.esprit.studentmanagement.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.studentmanagement.config.SecurityConfig;
import tn.esprit.studentmanagement.dto.RequestMapper;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.services.IEnrollment;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@Import(SecurityConfig.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IEnrollment enrollmentService;

    @MockitoBean
    private RequestMapper requestMapper;

    @Test
    @WithMockUser
    void getAllEnrollmentsReturnsOk() throws Exception {
        when(enrollmentService.getAllEnrollments()).thenReturn(List.of(new Enrollment()));

        mockMvc.perform(get("/Enrollment/getAllEnrollment"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEnrollmentsWithoutAuthReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/Enrollment/getAllEnrollment"))
                .andExpect(status().isUnauthorized());
    }
}
