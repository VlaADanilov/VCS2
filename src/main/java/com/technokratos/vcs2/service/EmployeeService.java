package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
public interface EmployeeService {

    List<EmployeeResponseDto> getAllEmployees();

    void delete(UUID empId);

    void add(@Valid EmployeeRequestDto employee);
}
