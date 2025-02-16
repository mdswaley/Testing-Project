package com.example.testing.Service;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Entity.EmployeeClass;
import com.example.testing.Repository.EmployeeRepository;
import com.example.testing.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class) // Enables Mockito for unit testing
class EmployeeServiceTest {

    @Mock // this mock is add inside employeeService
    private EmployeeRepository employeeRepository;

    @Spy // spy means we are not mock modelMapper. we use original mapper(bcz we know it will work as expected)
    private ModelMapper modelMapper;

    @InjectMocks // this is use for inject the mocks and create a bean as well
    private EmployeeService employeeService;

    @Test
    void testGetEmpById_whenEmpIsPresent_thenReturnEmpDto(){
//        Given
        Long id = 1L;
        EmployeeClass mockEmp = EmployeeClass.builder()
                .name("MD")
                .email("mdswaley@gmail.com")
                .age(21)
                .salary(20000)
                .build();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmp));
//        When
        EmployeeDto employeeDto = employeeService.getById(id);
//        Then
        assertThat(employeeDto.getId()).isEqualTo(mockEmp.getId());
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmp.getEmail());
        verify(employeeRepository).findById(id); // it says that whether the given method was called or
        // interact with mock or not.

        verify(employeeRepository, times(1)).findById(id); // verifying how many times it was called.
        // or atMost(5) means 5 or less ,
        // only() means only findById was called,
        // timeout(1000L) means verifying method was called with in given second.
        // time(no. of time) means verifying method was called given no. of times or not.
        // never() means verifying that a method was never called.
    }
}