package com.wayqui.covid19.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Covid19Statistic {

    @Id
    private String Id;

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
