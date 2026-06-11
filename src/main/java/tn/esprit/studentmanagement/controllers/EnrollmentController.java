package tn.esprit.studentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.EnrollmentRequest;
import tn.esprit.studentmanagement.dto.RequestMapper;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.services.IEnrollment;

import java.util.List;

@RestController
@RequestMapping("/Enrollment")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EnrollmentController {

    private IEnrollment enrollmentService;
    private RequestMapper requestMapper;

    @GetMapping("/getAllEnrollment")
    public List<Enrollment> getAllEnrollment() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/getEnrollment/{id}")
    public Enrollment getEnrollment(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PostMapping("/createEnrollment")
    public Enrollment createEnrollment(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.saveEnrollment(requestMapper.toEnrollment(request));
    }

    @PutMapping("/updateEnrollment")
    public Enrollment updateEnrollment(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.saveEnrollment(requestMapper.toEnrollment(request));
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}
