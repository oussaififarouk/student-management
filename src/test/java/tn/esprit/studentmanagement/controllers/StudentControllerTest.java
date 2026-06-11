package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.studentmanagement.config.SecurityConfig;
import tn.esprit.studentmanagement.dto.RequestMapper;
import tn.esprit.studentmanagement.dto.StudentRequest;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    @WithMockUser
    void createStudentReturnsOk() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");

        Student student = new Student();
        when(requestMapper.toStudent(any(StudentRequest.class))).thenReturn(student);
        when(studentService.saveStudent(student)).thenReturn(student);

        mockMvc.perform(post("/students/createStudent")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
