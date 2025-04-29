package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.EmployeeNotFoundException;
import com.technokratos.vcs2.model.Role;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> all = employeeRepository.findAll();
        return all.stream().map(a -> new EmployeeResponseDto(
                a.getId(),
                a.getName(),
                a.getProfession(),
                a.getDescription(),
                a.getAccount().getUsername(),
                a.getPhone()
        )).toList();
    }

    @Override
    public void delete(UUID empId) {
        Employee employee = employeeRepository
                .findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException(empId));
        employee.getAccount().setRole(Role.ROLE_DEFAULT.toString());
        employeeRepository.delete(employee);
    }
}
