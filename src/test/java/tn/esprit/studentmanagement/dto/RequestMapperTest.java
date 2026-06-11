package tn.esprit.studentmanagement.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.CourseRepository;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RequestMapper requestMapper;

    @Test
    void toStudentMapsFieldsAndDepartment() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setDepartmentId(1L);

        Department department = new Department();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Student student = requestMapper.toStudent(request);

        assertEquals("John", student.getFirstName());
        assertEquals(department, student.getDepartment());
    }

    @Test
    void toStudentThrowsWhenDepartmentMissing() {
        StudentRequest request = new StudentRequest();
        request.setDepartmentId(99L);
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> requestMapper.toStudent(request));
    }
}
