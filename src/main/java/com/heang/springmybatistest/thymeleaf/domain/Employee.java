package com.heang.springmybatistest.thymeleaf.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Data → generates getters, setters, toString, equals, hashCode
// @Builder → lets you create objects like: Employee.builder().name("Heang").build()
// @NoArgsConstructor → generates empty constructor: new Employee()
// @AllArgsConstructor → generates constructor with all fields
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Long id;

    // Simple field — access in Thymeleaf: ${employee.name}
    private String name;

    private String position;   // job title, e.g. "Developer", "Manager"

    private Integer salary;    // monthly salary — Integer (not int) so it can be null

    private boolean active;    // true = currently employed, false = left the company

    // Nested object — access in Thymeleaf: ${employee.department.name}
    // This is the main point of Level 2.1
    private Department department;

    // Another nested object — access in Thymeleaf: ${employee.address.city}
    private Address address;
}
