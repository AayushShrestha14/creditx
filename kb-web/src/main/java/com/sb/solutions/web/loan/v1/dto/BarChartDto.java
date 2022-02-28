package com.sb.solutions.web.loan.v1.dto;

import java.util.ArrayList;
import java.util.List;

public class BarChartDto {

    private String name;

    private List<SeriesDto> series = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SeriesDto> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesDto> series) {
        this.series = series;
    }
}
