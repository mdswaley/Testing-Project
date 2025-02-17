package com.example.testing.Service;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Entity.EmployeeClass;
import com.example.testing.Exceptions.ResourceNotFoundException;
import com.example.testing.Repository.EmployeeRepository;
import com.example.testing.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
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

    @Mock
    private EmployeeClass mockEmp;

    @Mock
    private EmployeeDto mockEmpDto;

    @BeforeEach
    void setUp(){
        mockEmp = EmployeeClass.builder()
                .id(1L)
                .name("MD")
                .email("mdswaley@gmail.com")
                .age(21)
                .salary(20000)
                .build();

        mockEmpDto = modelMapper.map(mockEmp,EmployeeDto.class);
    }

    @Test
    void testGetEmpById_whenEmpIsPresent_thenReturnEmpDto(){
//        Given
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmp));
//        When
        EmployeeDto employeeDto = employeeService.getById(id);
//        Then
        assertThat(employeeDto).isNotNull();
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

//    if employee with id is not found
    @Test
    void whenEmpWithId_isNotPresent_ThrowAnException(){
//        given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

//        when and then
        assertThatThrownBy(()-> employeeService.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
    }

//    create emp test
    @Test
    void testCreateNewEmp_whenValidEmp_thenCreateNew(){
//        given
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(EmployeeClass.class))).thenReturn(mockEmp);
//        when
        EmployeeDto employeeDto = employeeService.addEmp(mockEmpDto);
//        then

        ArgumentCaptor<EmployeeClass> employeeClassArrgumentCaptor = ArgumentCaptor.forClass(EmployeeClass.class);
        verify(employeeRepository).save(employeeClassArrgumentCaptor.capture());

        EmployeeClass captorEmp = employeeClassArrgumentCaptor.getValue();

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmp.getEmail());
        assertThat(captorEmp.getEmail()).isEqualTo(mockEmp.getEmail());
        verify(employeeRepository).save(any(EmployeeClass.class));
    }
}