package tn.esprit.studentmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "students")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDepartment;
    @NotBlank
    private String name;
    private String location;
    private String phone;
    private String head; // chef de département

    @OneToMany(mappedBy = "department")
    private List<Student> students;
}
