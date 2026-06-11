package tn.esprit.studentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService implements IEnrollment {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long idEnrollment) {
        return enrollmentRepository.findById(idEnrollment)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", idEnrollment));
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long idEnrollment) {
        if (!enrollmentRepository.existsById(idEnrollment)) {
            throw new ResourceNotFoundException("Enrollment", idEnrollment);
        }
        enrollmentRepository.deleteById(idEnrollment);
    }
}
