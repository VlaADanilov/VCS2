package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.EmployeeNotFoundException;
import com.technokratos.vcs2.exception.notFound.UserNotFoundException;
import com.technokratos.vcs2.model.Role;
import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.EmployeeRepository;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserServiceImpl userService;

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
        userService.doDefault(employee.getAccount().getId());
        employeeRepository.delete(employee);
    }

    @Override
    public void add(EmployeeRequestDto employee) {
        User byUsername = userService.findByUsername(employee.getAccountName());
        Employee empl = Employee.builder()
                .id(UUID.randomUUID())
                .phone(employee.getPhone())
                .name(employee.getName())
                .description(employee.getDescription())
                .profession(employee.getProfession())
                .account(byUsername)
                .build();
        employeeRepository.save(empl);
        userService.doModerator(byUsername.getId());
    }
}
