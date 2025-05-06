package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.ImageExistInThisEmployeeException;
import com.technokratos.vcs2.exception.notFound.EmployeeNotFoundException;
import com.technokratos.vcs2.mapper.EmployeeMapper;
import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.EmployeeRepository;
import com.technokratos.vcs2.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserServiceImpl userService;
    private final EmployeeMapper employeeMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        log.info("Fetching all employees from the database");
        List<Employee> all = employeeRepository.findAll();
        List<EmployeeResponseDto> responseDtos = all.stream()
                .map(employeeMapper::toResponse)
                .toList();

        log.info("Found {} employees", responseDtos.size());
        return responseDtos;
    }

    @Override
    public void delete(UUID empId) {
        log.info("Deleting employee with ID: {}", empId);

        Employee employee = employeeRepository
                .findById(empId)
                .orElseThrow(() -> {
                    log.warn("Employee with ID {} not found", empId);
                    return new EmployeeNotFoundException(empId);
                });

        log.info("Reverting user {} to default role", employee.getAccount().getId());
        userService.doDefault(employee.getAccount().getId());

        if (employee.getImage() != null) {
            log.info("Deleting associated image {} for employee {}", employee.getImage().getId(), empId);
            imageService.deleteImageFromEmployee(employee.getImage().getId(), empId);
        }

        employeeRepository.delete(employee);
        log.info("Employee with ID {} deleted successfully", empId);
    }

    @Override
    public void add(EmployeeRequestDto employee) {
        log.info("Adding new employee with account name: {}", employee.getAccountName());

        User byUsername = userService.findByUsername(employee.getAccountName());
        Employee empl = employeeMapper.toEntity(employee, byUsername);
        employeeRepository.save(empl);
        log.info("Employee saved with ID: {}", empl.getId());

        log.info("Promoting user {} to moderator role", byUsername.getId());
        userService.doModerator(byUsername.getId());
    }

    @Override
    public void checkForExistsEmployee(UUID employeeId) {
        log.debug("Checking if employee with ID {} exists", employeeId);

        if (!employeeRepository.existsById(employeeId)) {
            log.warn("Employee with ID {} does not exist", employeeId);
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Override
    public void addImageToEmployee(UUID employeeId, UUID imageId) {
        log.info("Adding image {} to employee {}", imageId, employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    log.warn("Employee with ID {} not found", employeeId);
                    return new EmployeeNotFoundException(employeeId);
                });

        if (employee.getImage() != null) {
            log.warn("Employee {} already has an image", employeeId);
            throw new ImageExistInThisEmployeeException();
        }

        employee.setImage(imageRepository.getReferenceById(imageId));
        employeeRepository.save(employee);
        log.info("Image added successfully to employee {}", employeeId);
    }
}
