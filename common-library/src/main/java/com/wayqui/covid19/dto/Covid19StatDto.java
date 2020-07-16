package com.wayqui.covid19.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    public String getStatistics() {
        return "Covid19StatDto{" +
                ", countryCode='" + countryCode + '\'' +
                ", date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deaths=" + deaths +
                '}'; 
    }
}
