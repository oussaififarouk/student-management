package tn.esprit.studentmanagement.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Course;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.CourseRepository;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void toStudentWithoutDepartmentId() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("Jane");

        Student student = requestMapper.toStudent(request);

        assertEquals("Jane", student.getFirstName());
        assertNull(student.getDepartment());
    }

    @Test
    void toStudentThrowsWhenDepartmentMissing() {
        StudentRequest request = new StudentRequest();
        request.setDepartmentId(99L);
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> requestMapper.toStudent(request));
    }

    @Test
    void toDepartmentMapsAllFields() {
        DepartmentRequest request = new DepartmentRequest();
        request.setIdDepartment(1L);
        request.setName("IT");
        request.setLocation("Building A");
        request.setPhone("123");
        request.setHead("Alice");

        Department department = requestMapper.toDepartment(request);

        assertEquals("IT", department.getName());
        assertEquals("Alice", department.getHead());
    }

    @Test
    void toEnrollmentMapsFields() {
        EnrollmentRequest request = new EnrollmentRequest();
        request.setIdEnrollment(5L);
        request.setEnrollmentDate(LocalDate.of(2026, 1, 15));
        request.setGrade(15.5);
        request.setStatus(Status.ACTIVE);
        request.setStudentId(1L);
        request.setCourseId(2L);

        Student student = new Student();
        Course course = new Course();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));

        Enrollment enrollment = requestMapper.toEnrollment(request);

        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());
        assertEquals(Status.ACTIVE, enrollment.getStatus());
    }

    @Test
    void toEnrollmentThrowsWhenStudentMissing() {
        EnrollmentRequest request = new EnrollmentRequest();
        request.setStudentId(1L);
        request.setCourseId(2L);
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> requestMapper.toEnrollment(request));
    }

    @Test
    void toEnrollmentThrowsWhenCourseMissing() {
        EnrollmentRequest request = new EnrollmentRequest();
        request.setStudentId(1L);
        request.setCourseId(2L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> requestMapper.toEnrollment(request));
    }
}
