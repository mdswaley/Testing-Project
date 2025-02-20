package com.example.testing.Service;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Entity.EmployeeClass;
import com.example.testing.Exceptions.ResourceNotFoundException;
import com.example.testing.Repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public List<EmployeeDto> getAllEmp(){
        log.info("Fetching Get All Employee");
        List<EmployeeClass> emp = employeeRepository.findAll();
        return emp.stream()
                .map(emp1 -> modelMapper.map(emp1,EmployeeDto.class))
                .collect(Collectors.toList());
    }

    public EmployeeDto getById(Long id){
        log.info("Fetching Get Employee by Id "+id);
        EmployeeClass emp = employeeRepository.findById(id)
                .orElseThrow(()->{
                    log.error("Employee not found with id: {}",id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        log.info("Successfully fetched employee with id: {}", id);
        return modelMapper.map(emp,EmployeeDto.class);
    }

    public EmployeeDto addEmp(EmployeeDto employeeDto){
        log.info("Creating new employee with email: {}", employeeDto.getEmail());
        List<EmployeeClass> existingEmployees = employeeRepository.findByEmail(employeeDto.getEmail());
        if (!existingEmployees.isEmpty()) {
            log.error("Employee already exists with email: {}", employeeDto.getEmail());
            throw new RuntimeException("Employee already exists with email: " + employeeDto.getEmail());
        }
        EmployeeClass emp = modelMapper.map(employeeDto,EmployeeClass.class);
        EmployeeClass saveEmp = employeeRepository.save(emp);
        log.info("Successfully created new employee with id: {}", emp.getId());
        return modelMapper.map(saveEmp,EmployeeDto.class);
    }

    public EmployeeDto updateEmp(Long id, EmployeeDto employeeDto){
        log.info("Updating employee with id: {}", id);

        EmployeeClass employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        if (!employee.getEmail().equals(employeeDto.getEmail())) {
            log.error("Attempted to update email for employee with id: {}", id);
            throw new RuntimeException("The email of the employee cannot be updated");
        }

        modelMapper.map(employeeDto, employee);
        employee.setId(id);

        EmployeeClass savedEmployee = employeeRepository.save(employee);
        log.info("Successfully updated employee with id: {}", id);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    public String deleteEmp(Long id){
        log.info("Deleting employee with id: {}", id);
        boolean exists = employeeRepository.existsById(id);
        if (!exists) {
            log.error("Employee not found with id: {}", id);
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        employeeRepository.deleteById(id);
        log.info("Successfully deleted employee with id: {}", id);
        return "Successfully deleted employee with id: {}" + id;
    }
}
