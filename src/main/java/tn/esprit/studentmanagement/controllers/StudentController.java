package tn.esprit.studentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.RequestMapper;
import tn.esprit.studentmanagement.dto.StudentRequest;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class StudentController {

    private IStudentService studentService;
    private RequestMapper requestMapper;

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping("/createStudent")
    public Student createStudent(@Valid @RequestBody StudentRequest request) {
        return studentService.saveStudent(requestMapper.toStudent(request));
    }

    @PutMapping("/updateStudent")
    public Student updateStudent(@Valid @RequestBody StudentRequest request) {
        return studentService.saveStudent(requestMapper.toStudent(request));
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
