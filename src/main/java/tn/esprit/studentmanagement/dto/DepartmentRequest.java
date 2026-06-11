package tn.esprit.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequest {

    private Long idDepartment;

    @NotBlank
    private String name;

    private String location;

    private String phone;

    private String head;
}
