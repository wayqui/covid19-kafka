package com.wayqui.covid19.api;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsListResponse {
    private List<ApiStatisticResponse> employees;
}
