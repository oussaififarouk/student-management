package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.exceptions.ResourceNotFoundException;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void getAllDepartmentsReturnsRepositoryResult() {
        Department department = new Department();
        when(departmentRepository.findAll()).thenReturn(List.of(department));

        assertEquals(1, departmentService.getAllDepartments().size());
    }

    @Test
    void getDepartmentByIdReturnsDepartmentWhenFound() {
        Department department = new Department();
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(department));

        assertEquals(department, departmentService.getDepartmentById(2L));
    }

    @Test
    void getDepartmentByIdThrowsWhenMissing() {
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(2L));
    }

    @Test
    void saveDepartmentDelegatesToRepository() {
        Department department = new Department();
        when(departmentRepository.save(department)).thenReturn(department);

        assertEquals(department, departmentService.saveDepartment(department));
    }

    @Test
    void deleteDepartmentThrowsWhenMissing() {
        when(departmentRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> departmentService.deleteDepartment(2L));
    }

    @Test
    void deleteDepartmentRemovesExistingDepartment() {
        when(departmentRepository.existsById(2L)).thenReturn(true);

        departmentService.deleteDepartment(2L);

        verify(departmentRepository).deleteById(2L);
    }
}
