package com.infirmary.backend.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDataDTO {
    private String name;
    private Double longitude;
    private Double latitude;
}
