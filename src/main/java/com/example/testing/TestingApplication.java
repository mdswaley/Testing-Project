package com.example.testing;

import com.example.testing.Service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TestingApplication implements CommandLineRunner {

    private final DataService dataService;

    @Value("${my.variable}")
    private String myVariable;

    public static void main(String[] args) {
        SpringApplication.run(TestingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("my variable is : "+myVariable);
        System.out.println("data is "+dataService.getData());
    }
}
