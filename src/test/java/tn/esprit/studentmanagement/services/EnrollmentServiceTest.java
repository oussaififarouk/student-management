package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void getAllEnrollmentsReturnsRepositoryResult() {
        Enrollment enrollment = new Enrollment();
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        assertEquals(1, enrollmentService.getAllEnrollments().size());
    }

    @Test
    void getEnrollmentByIdReturnsEnrollmentWhenFound() {
        Enrollment enrollment = new Enrollment();
        when(enrollmentRepository.findById(3L)).thenReturn(Optional.of(enrollment));

        assertEquals(enrollment, enrollmentService.getEnrollmentById(3L));
    }

    @Test
    void getEnrollmentByIdThrowsWhenMissing() {
        when(enrollmentRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.getEnrollmentById(3L));
    }

    @Test
    void saveEnrollmentDelegatesToRepository() {
        Enrollment enrollment = new Enrollment();
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        assertEquals(enrollment, enrollmentService.saveEnrollment(enrollment));
    }

    @Test
    void deleteEnrollmentThrowsWhenMissing() {
        when(enrollmentRepository.existsById(3L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.deleteEnrollment(3L));
    }

    @Test
    void deleteEnrollmentRemovesExistingEnrollment() {
        when(enrollmentRepository.existsById(3L)).thenReturn(true);

        enrollmentService.deleteEnrollment(3L);

        verify(enrollmentRepository).deleteById(3L);
    }
}
