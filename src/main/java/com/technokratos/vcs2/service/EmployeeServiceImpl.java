package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.EmployeeNotFoundException;
import com.technokratos.vcs2.mapper.EmployeeMapper;
import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.EmployeeRepository;
import com.technokratos.vcs2.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserServiceImpl userService;
    private final EmployeeMapper employeeMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> all = employeeRepository.findAll();
        return all.stream().map(employeeMapper::toResponse).toList();
    }

    @Override
    public void delete(UUID empId) {
        Employee employee = employeeRepository
                .findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException(empId));
        userService.doDefault(employee.getAccount().getId());

        if (employee.getImage() != null) {
            imageService.deleteImageFromEmployee(employee.getImage().getId(), empId);
        }

        employeeRepository.delete(employee);
    }

    @Override
    public void add(EmployeeRequestDto employee) {
        User byUsername = userService.findByUsername(employee.getAccountName());
        Employee empl = employeeMapper.toEntity(employee, byUsername);
        employeeRepository.save(empl);
        userService.doModerator(byUsername.getId());
    }

    @Override
    public void checkForExistsEmployee(UUID employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Override
    public void addImageToEmployee(UUID employeeId, UUID imageId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        if (employee.getImage() != null) {
            throw new RuntimeException("Image already exists");
        }
        employee.setImage(imageRepository.getReferenceById(imageId));
        employeeRepository.save(employee);
    }
}
