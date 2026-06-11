package tn.esprit.studentmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.studentmanagement.entities.Status;

import java.time.LocalDate;

@Getter
@Setter
public class EnrollmentRequest {

    private Long idEnrollment;

    @NotNull
    private LocalDate enrollmentDate;

    private Double grade;

    @NotNull
    private Status status;

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;
}
