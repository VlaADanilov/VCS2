package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.EmployeeApi;
import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.service.EmployeeService;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {
    private final EmployeeService employeeService;


    public String getEmployee(Model model) {
        model.addAttribute("list", employeeService.getAllEmployees());
        model.addAttribute("can", canCRUD());
        return "list_of_employees";
    }


    public void deleteEmployee(UUID emp_id) {
        employeeService.delete(emp_id);
    }


    public void addEmployee(EmployeeRequestDto employee) {
        employeeService.add(employee);
    }

    public String getAddEmployeeForm() {
        return "add_employee_form";
    }

    private boolean canCRUD() {
        Optional<User> currentUser = UserReturner.getCurrentUser();
        return currentUser.map(user -> user.getRole().equals("ROLE_ADMIN")).orElse(false);
    }
}
