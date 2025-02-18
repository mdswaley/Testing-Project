package com.example.testing.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private Integer salary;
}
