package com.heang.springmybatistest.thymeleaf.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    // Access in Thymeleaf: ${employee.address.city}
    private String city;       // e.g. "Seoul", "Busan"

    private String district;   // e.g. "Gangnam-gu", "Mapo-gu"

    private String zipCode;    // e.g. "06000"
}
