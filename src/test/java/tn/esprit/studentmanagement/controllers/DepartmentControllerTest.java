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
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@Import(SecurityConfig.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IDepartmentService departmentService;

    @MockitoBean
    private RequestMapper requestMapper;

    @Test
    @WithMockUser
    void getAllDepartmentsReturnsOk() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(new Department()));

        mockMvc.perform(get("/department/getAllDepartment"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllDepartmentsWithoutAuthReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/department/getAllDepartment"))
                .andExpect(status().isUnauthorized());
    }
}
