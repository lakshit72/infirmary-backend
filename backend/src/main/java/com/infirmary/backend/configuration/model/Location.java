package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.LocationDataDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "locations")
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locId;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public Location(LocationDataDTO locationDataDTO){
        this.locationName = locationDataDTO.getName();
        this.latitude = locationDataDTO.getLatitude();
        this.longitude = locationDataDTO.getLongitude();
    }

}
