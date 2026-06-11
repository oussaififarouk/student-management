package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getAllStudentsReturnsRepositoryResult() {
        Student student = new Student();
        when(studentRepository.findAll()).thenReturn(List.of(student));

        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    void getStudentByIdReturnsStudentWhenFound() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertEquals(student, studentService.getStudentById(1L));
    }

    @Test
    void getStudentByIdThrowsWhenMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void saveStudentDelegatesToRepository() {
        Student student = new Student();
        when(studentRepository.save(student)).thenReturn(student);

        assertEquals(student, studentService.saveStudent(student));
    }

    @Test
    void deleteStudentThrowsWhenMissing() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }

    @Test
    void deleteStudentRemovesExistingStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }
}
