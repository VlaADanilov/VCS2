package com.technokratos.vcs2.mapper;

import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeMapper {
    
    public EmployeeResponseDto toResponse(Employee employee) {
        UUID imageId = null;
        if (employee.getImage() != null) {
            imageId = employee.getImage().getId();
        }
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getProfession(),
                employee.getDescription(),
                employee.getAccount().getUsername(),
                employee.getPhone(),
                imageId
        );
    }

    public Employee toEntity(EmployeeRequestDto employee, User user) {
        return Employee.builder()
                .id(UUID.randomUUID())
                .phone(employee.getPhone())
                .name(employee.getName())
                .description(employee.getDescription())
                .profession(employee.getProfession())
                .account(user)
                .build();
    }
}
