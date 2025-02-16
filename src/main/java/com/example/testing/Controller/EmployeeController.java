package com.example.testing.Controller;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmp(){
        return ResponseEntity.ok(employeeService.getAllEmp());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> addEmp(@RequestParam EmployeeDto emp){
        return ResponseEntity.ok(employeeService.addEmp(emp));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmp(@PathVariable Long id,@RequestParam EmployeeDto emp){
        return new ResponseEntity<>(employeeService.updateEmp(id,emp), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.deleteEmp(id));
    }

}
