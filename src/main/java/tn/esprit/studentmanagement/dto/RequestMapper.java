package tn.esprit.studentmanagement.dto;

import tn.esprit.studentmanagement.entities.Course;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.CourseRepository;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public Student toStudent(StudentRequest request) {
        Student student = new Student();
        student.setIdStudent(request.getIdStudent());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDepartmentId()));
            student.setDepartment(department);
        }

        return student;
    }

    public Department toDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setIdDepartment(request.getIdDepartment());
        department.setName(request.getName());
        department.setLocation(request.getLocation());
        department.setPhone(request.getPhone());
        department.setHead(request.getHead());
        return department;
    }

    public Enrollment toEnrollment(EnrollmentRequest request) {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(request.getIdEnrollment());
        enrollment.setEnrollmentDate(request.getEnrollmentDate());
        enrollment.setGrade(request.getGrade());
        enrollment.setStatus(request.getStatus());

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollment;
    }
}
