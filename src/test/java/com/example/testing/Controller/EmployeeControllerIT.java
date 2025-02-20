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
        testEmployeeDto = modelMapper.map(testEmployeeClass,EmployeeDto.class);
        testEmployeeDto.setId(1L);
    }

    @Test
    void testGetEmpById_ifSuccess(){
        EmployeeClass savedEmp = employeeRepository.save(testEmployeeClass);
        webTestClient.get()
                .uri("/emp/{id}",savedEmp.getId()) //in uri already define baseUrl like (localhost://8080) no need to define
                .exchange() // it takes all request and give response
                .expectStatus() // it is like assert we can check whether data is found or not like wise.
                .isOk()
                .expectBody(EmployeeDto.class) // this map our json data to class fields
//                .isEqualTo(testEmployeeDto); // make sure DTO class should have equals and hashCode implementation.
//                Else we can use value which take emp from response body then make sure are equals to the expected value.
                .value(employeeDto -> {
                    assertThat(employeeDto.getEmail()).isEqualTo(testEmployeeDto.getEmail());
                    assertThat(employeeDto.getId()).isEqualTo(testEmployeeDto.getId());
                });
    }

}