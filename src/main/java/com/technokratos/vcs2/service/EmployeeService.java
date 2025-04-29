package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponseDto> getAllEmployees();

    void delete(UUID empId);
}
