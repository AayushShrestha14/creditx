package com.sb.solutions.core.utils.csv;

import java.util.Map;

/**
 * Created by Rujan Maharjan on 3/4/2019.
 */
public class CsvHeader {

    private String header;
    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public CsvHeader(String header) {
        this.header = header;
    }


    public CsvHeader() {
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
