package tn.esprit.studentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", id);
        }
        studentRepository.deleteById(id);
    }
}
