package com.heang.springmybatistest.thymeleaf.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    private Long id;

    // Access in Thymeleaf: ${employee.department.name}
    private String name;         // e.g. "Engineering", "HR", "Finance"

    private String location;     // e.g. "Seoul HQ", "Busan Office"

    private Integer headCount;   // number of people in this department
}
