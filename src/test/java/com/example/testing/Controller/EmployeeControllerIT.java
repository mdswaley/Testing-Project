package com.example.testing.Controller;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Entity.EmployeeClass;
import com.example.testing.Repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerIT extends AbstractIntegrationTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

//  get employee by id
    @Test
    void testGetEmpById_ifSuccess(){
        EmployeeClass savedEmp = employeeRepository.save(testEmployeeClass);

        webTestClient.get()
                .uri("/emp/{id}",savedEmp.getId()) //in uri already define baseUrl like (localhost://8080) no need to define
                .exchange() // it executes the request and return a webClient response
                .expectStatus() // it is like assert return the status code of response
                .isOk()
                .expectBody(EmployeeDto.class) // assert the body of response.
//                .isEqualTo(testEmployeeDto); // make sure DTO class should have equals and hashCode implementation.
//                Else we can use value which take emp from response body then make sure are equals to the expected value.
                .value(employeeDto -> {
                    assertThat(employeeDto.getEmail()).isEqualTo(savedEmp.getEmail());
                    assertThat(employeeDto.getId()).isEqualTo(savedEmp.getId());
                });
    }

    @Test
    void testGetEmpById_ifNotFound(){
        webTestClient.get()
                .uri("/emp/1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testGetEmployee_AllEmployee(){
        webTestClient.get()
                .uri("/emp")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class);
    }

//    create new employee
    @Test
    void testCreateEmp_whenEmpAlreadyExist_throwException(){
        EmployeeClass savedEmp = employeeRepository.save(testEmployeeClass);

        webTestClient.post()
                .uri("/emp")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testCreate_whenEmpIsNotExist_thenCreateEmployee(){

        webTestClient.post()
                .uri("/emp")
                .bodyValue(testEmployeeClass)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail()); // Verifies that the 'email' field in the JSON response matches testEmployeeDto.getEmail()
    }

//    Test update employee
    @Test
    void testUpdateEmployee_whenEmpIsNotExist_thenThrowException(){
        webTestClient.put()
                .uri("/emp/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptedToUpdateEmail_thenThrowException(){
        EmployeeClass savedEmp = employeeRepository.save(testEmployeeClass);
        testEmployeeDto.setName("MD");
        testEmployeeDto.setEmail("random@gmail.com");

        webTestClient.put()
                .uri("/emp/{id}",savedEmp.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee(){
        EmployeeClass savedEmp = employeeRepository.save(testEmployeeClass);
        testEmployeeDto.setName("raja");
        testEmployeeDto.setAge(23);
        testEmployeeDto.setSalary(4000);

        webTestClient.put()
                .uri("/emp/{id}",savedEmp.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }

//    test Delete Employee

    @Test
    void testDeleteEmployee_whenEmployeeIsNotFound_thenThrowException(){
        webTestClient.delete()
                .uri("/emp/999")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeIsExist_thenDelete(){
        EmployeeClass savedEmp = employeeRepository.save(testEmployeeClass);

        webTestClient.delete()
                .uri("/emp/{id}",savedEmp.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class);

        // if employee is deleted then it should return not found
        webTestClient.delete()
                .uri("/emp/1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}