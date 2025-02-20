package com.example.testing.Controller;

import com.example.testing.DTO.EmployeeDto;
import com.example.testing.Entity.EmployeeClass;
import com.example.testing.TestContainerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestContainerConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // This starts the application with a random available port and allows WebTestClient to interact with it
@AutoConfigureWebTestClient(timeout = "100000")
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    EmployeeClass testEmployeeClass = EmployeeClass.builder()
            .name("Swaley")
                .age(21)
                .salary(5000)
                .email("md@gmail.com")
                .build();

    EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .id(1L)
                .name("Swaley")
                .age(21)
                .salary(5000)
                .email("md@gmail.com")
                .build();

}
