package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.dto.request.EmployeeRequestDto;
import com.technokratos.vcs2.model.dto.response.EmployeeResponseDto;
import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.service.EmployeeService;
import com.technokratos.vcs2.service.UserServiceImpl;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public String getEmployee(Model model) {
        model.addAttribute("list", employeeService.getAllEmployees());
        model.addAttribute("can", canCRUD());
        return "list_of_employees";
    }

    @DeleteMapping("/{emp_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteEmployee(@PathVariable("emp_id") UUID emp_id) {
        employeeService.delete(emp_id);
    }

    @PostMapping("/add")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmployee(@RequestBody EmployeeRequestDto employee) {
        employeeService.add(employee);
    }

    private boolean canCRUD() {
        Optional<User> currentUser = UserReturner.getCurrentUser();
        return currentUser.map(user -> user.getRole().equals("ROLE_ADMIN")).orElse(false);
    }
}
