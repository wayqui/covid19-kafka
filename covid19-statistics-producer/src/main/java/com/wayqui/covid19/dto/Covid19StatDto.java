package com.wayqui.covid19.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Covid19StatDto {

    private String country;
    private String countryCode;
    private String province;
    private String city;
    private String cityCode;
    private Float latitude;
    private Float longitude;
    private Long confirmed;
    private Long deaths;
    private Long recovered;
    private Long active;
    private LocalDateTime date;
}
