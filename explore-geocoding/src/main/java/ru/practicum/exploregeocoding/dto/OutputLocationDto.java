package ru.practicum.exploregeocoding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

/**
 * DTO локации - ответ основному сервису
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class OutputLocationDto {
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String street;
    private String houseNumber;
    private String type;
    private Boolean resolved;
    private String resolveDate;
}
