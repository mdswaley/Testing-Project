package com.example.testing.Service;

import com.example.testing.Entity.EmployeeClass;
import com.example.testing.Repository.EmployeeRepository;
import com.example.testing.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import static org.mockito.Mockito.*;

import java.util.Optional;

@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class) // Enables Mockito for unit testing
class EmployeeServiceTest {

    @Mock // this mock is add inside employeeService
    private EmployeeRepository employeeRepository;

    @InjectMocks // this is use for inject the mocks and create a bean as well
    private EmployeeService employeeService;

    @Test
    void testGetEmpById_whenEmpIsPresent_thenReturnEmpDto(){
//        Given
        Long id = 1L;
        EmployeeClass emp = EmployeeClass.builder()
                .name("MD")
                .email("mdswaley@gmail.com")
                .age(21)
                .salary(20000)
                .build();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));
//        When

//        Then


    }
}