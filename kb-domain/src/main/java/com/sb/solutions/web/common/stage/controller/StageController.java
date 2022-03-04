package com.sb.solutions.web.common.stage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.web.common.stage.dto.StageDto;

/**
 * @author Sunil Babu Shrestha on 5/26/2019
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/stage")
public class StageController {

    @PostMapping("/stage")
    public ResponseEntity<?> performActionOnDocument(@RequestBody StageDto stageDto) {
        return null;
    }
}
