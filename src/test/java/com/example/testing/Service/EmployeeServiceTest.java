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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

//   Test for Return all Employee
    @Test
    void testGetAllEmp_whenEmpIsPreset(){
        when(employeeRepository.findAll()).thenReturn(List.of(mockEmp));
        List<EmployeeDto> emp = employeeService.getAllEmp();

        assertThat(emp).isNotNull();
        assertThat(emp).isNotEmpty();
        assertThat(emp.size()).isEqualTo(1);
    }

    @Test
    void testGetAllEmp_whenNotPresent(){
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<EmployeeDto> result = employeeService.getAllEmp();

        assertTrue(result.isEmpty());
    }

//    if employee with id is not found
    @Test
    void whenEmpWithId_isNotPresent_ThrowAnException(){
//        given
        when(employeeRepository.findById(mockEmp.getId())).thenReturn(Optional.empty());

//        when and then
        assertThatThrownBy(()-> employeeService.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
    }

//    create emp test
    @Test
    void testCreateNewEmp_whenValidEmp_thenCreateNew(){
//        given
        when(employeeRepository.findByEmail(mockEmp.getEmail())).thenReturn(List.of());
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

//    when add employee call if employee already exist then throw an exception
    @Test
    void testCreatNewEmp_ifEmpExist_thenThrowAnException(){
//        Given
        when(employeeRepository.findByEmail(mockEmp.getEmail())).thenReturn(List.of(mockEmp));

//        when and then
        assertThatThrownBy(()->employeeService.addEmp(mockEmpDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmp.getEmail());
        verify(employeeRepository,atMost(1)).findByEmail(mockEmp.getEmail());
        verify(employeeRepository,never()).save(any());
    }

//    Test for update employee (if emp doesn't exists)
    @Test
    void testEmpUpdate_whenEmpDoesNtExist_throwException(){
//        Assign
        when(employeeRepository.findById(mockEmp.getId())).thenReturn(Optional.empty());
//        Assert
        assertThatThrownBy(()->employeeService.updateEmp(mockEmp.getId(),mockEmpDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
//        Act
        verify(employeeRepository,only()).findById(1L);
        verify(employeeRepository,never()).save(mockEmp);
    }

//   if user want to change employee email
    @Test
    void testEmpUpdate_whenWantToUpdateEmail_thenThrowException(){
//        Assign
        when(employeeRepository.findById(mockEmp.getId())).thenReturn(Optional.of(mockEmp));
//        set new employee email
        mockEmpDto.setName("Raja");
        mockEmpDto.setEmail("raja@gmail.com");

//        Assert
        assertThatThrownBy(()->employeeService.updateEmp(mockEmp.getId(),mockEmpDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository,only()).findById(1L);
        verify(employeeRepository,never()).save(mockEmp);
    }


//    update the employee
    @Test
    void testEmpUpdate_whenEmpIsExist(){
//        assign
        when(employeeRepository.findById(mockEmp.getId())).thenReturn(Optional.of(mockEmp));
        mockEmpDto.setName("Raja");
        mockEmpDto.setAge(23);
        mockEmpDto.setSalary(6000);

        EmployeeClass newEmp = modelMapper.map(mockEmpDto,EmployeeClass.class);
        when(employeeRepository.save(any(EmployeeClass.class))).thenReturn(newEmp);

//        act
        EmployeeDto updateEmp = employeeService.updateEmp(mockEmpDto.getId(),mockEmpDto);

//        assert
        assertThat(updateEmp).isEqualTo(mockEmpDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());
    }

//    test for Delete employee
    @Test
    void testEmpDelete_whenEmpWithIdNotPresent_thenThrowException(){
        when(employeeRepository.existsById(mockEmp.getId())).thenReturn(false);

        assertThatThrownBy(()->employeeService.deleteEmp(mockEmp.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
        verify(employeeRepository,only()).existsById(mockEmp.getId());
        verify(employeeRepository,never()).deleteById(anyLong());
    }

    @Test
    void testEmpDelete_whenEmpExist_thenDelete(){
        when(employeeRepository.existsById(mockEmpDto.getId())).thenReturn(true);

//        we use assertThatCode when method doesn't return any think but may be thrown an exception
        assertThatCode(()->employeeService.deleteEmp(mockEmp.getId()))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(mockEmp.getId());

    }


}