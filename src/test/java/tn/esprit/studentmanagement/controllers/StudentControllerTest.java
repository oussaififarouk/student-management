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
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IStudentService studentService;

    @MockitoBean
    private RequestMapper requestMapper;

    @Test
    @WithMockUser
    void getAllStudentsRequiresAuthenticationAndReturnsOk() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(new Student()));

        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllStudentsWithoutAuthReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isUnauthorized());
    }
}
