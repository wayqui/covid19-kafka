package com.wayqui.covid19.api;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatisticResponse {

    @SerializedName("Country")
    private String country;
    @SerializedName("CountryCode")
    private String countryCode;
    @SerializedName("Province")
    private String province;
    @SerializedName("City")
    private String city;
    @SerializedName("CityCode")
    private String cityCode;
    @SerializedName("Lat")
    private String lat;
    @SerializedName("Lon")
    private String lon;
    @SerializedName("Confirmed")
    private Long confirmed;
    @SerializedName("Deaths")
    private Long deaths;
    @SerializedName("Recovered")
    private Long recovered;
    @SerializedName("Active")
    private Long active;
    @SerializedName("Date")
    private Date date;
}
