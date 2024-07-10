package com.vineet.springBootWeb.controllers;

import com.vineet.springBootWeb.dto.EmployeeDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "employees")
public class EmployeeController {
    //
    //    @GetMapping(path="/")
    //    public String getMessage() {
    //        return "Get Message function called";
    //    }

    @GetMapping(path="/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable Long employeeId) {
        return new EmployeeDTO(employeeId, "Vineet", "vineet@gmail.com", 25, LocalDate.of(2024, 1, 23), true);
    }

    @GetMapping
    public String getAllEmployees(@RequestParam(required = false) Integer age) {
    return "Hi Age "+age;
    }

    @PostMapping
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO inputEmployee) {
        inputEmployee.setId(100L);
        return inputEmployee;
    }

    @PutMapping
    public String updateEmployee() {
        return "Hello from PUT";
    }
}
