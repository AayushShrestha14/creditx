package com.sb.solutions.web.v1;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.date.NepaliDate;

/**
 * @author : Rujan Maharjan on  2/19/2020
 **/

@RestController
@RequestMapping(CalendarController.URL)
public class CalendarController {

    static final String URL = "v1/calendar";

    @GetMapping
    public ResponseEntity<?> getCalendar() {
        Map<String, Object> calendar = new LinkedHashMap<>();
        calendar.put("initialDate", NepaliDate.engAndNepStartDate());
        calendar.put("month", NepaliDate.MONTHS);
        calendar.put("calendar", new NepaliDate().calendarMap());
        return new RestResponseDto().successModel(calendar);
    }

}
