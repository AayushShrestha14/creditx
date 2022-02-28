package com.sb.solutions.api.cbsGroup.entity;

import static com.sb.solutions.core.constant.AppConstant.DATE_FORMAT;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.sb.solutions.api.loan.dto.LoanStageDto;

/**
 * @author : Rujan Maharjan on  12/21/2020
 **/
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CbsGroup extends AbstractPersistable<Long> {

    private String obligor;

    private String jsonData;

    private Date lastModifiedAt = new Date();

    @Transient
    private Map<String, Object> jsonDataMap;

    public Map<String, Object> getJsonDataMap() {
        if (this.getJsonData() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                this.jsonDataMap = objectMapper.readValue(this.getJsonData(),
                    Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.jsonDataMap = new HashMap<>();
        }
        return this.jsonDataMap;
    }


}
