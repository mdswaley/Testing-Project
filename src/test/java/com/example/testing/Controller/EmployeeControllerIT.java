package com.example.testing.Controller;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Entity.EmployeeClass;
import com.example.testing.Repository.EmployeeRepository;
import com.example.testing.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(TestContainerConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // This starts the application with a random available port and allows WebTestClient to interact with it
@AutoConfigureWebTestClient(timeout = "100000")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeClass testEmployeeClass;

    private EmployeeDto testEmployeeDto;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp(){
        testEmployeeClass = EmployeeClass.builder()
                .name("Swaley")
                .age(21)
                .salary(5000)
                .email("md@gmail.com")
                .build();

        testEmployeeDto = EmployeeDto.builder()
                .id(1L)
                .name("Swaley")
                .age(21)
                .salary(5000)
                .email("md@gmail.com")
                .build();

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
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail());
    }

}