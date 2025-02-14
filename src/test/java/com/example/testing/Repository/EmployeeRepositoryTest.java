package com.example.testing.Repository;

import com.example.testing.Entity.EmployeeClass;
import com.example.testing.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;


//@SpringBootTest // it execute whole project
@Import(TestContainerConfig.class)
@DataJpaTest // it executes only class Repository and Entity
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // to change our
// local database to in memory database or embedded database (h2)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeClass employeeClass;

    @BeforeEach
    void setUp(){
        employeeClass = EmployeeClass.builder()
                .name("MD")
                .email("md123@gmail.com")
                .age(21)
                .salary(20000)
                .build();
    }

    @Test
    void findByEmail_whenEmailIsPresent_returnEmployee() {
//        Given
        employeeRepository.save(employeeClass);

//        When
        List<EmployeeClass> emp = employeeRepository.findByEmail(employeeClass.getEmail());

//        Then
        assertThat(emp).isNotNull();
        assertThat(emp).isNotEmpty();
        assertThat(emp.get(0).getEmail()).isEqualTo(employeeClass.getEmail());
    }


    @Test
    void findByEmail_whenEmailIsNotFound_returnEmptyEmployeeList() {
//        Given
        String email = "abc@gmail.com";
//        When
        List<EmployeeClass> emp = employeeRepository.findByEmail(email);
//        Then
        assertThat(emp).isNotNull();
//        assertThat(emp).isNotEmpty(); // expected not empty but find empty list
        assertThat(emp).isEmpty();

    }
}