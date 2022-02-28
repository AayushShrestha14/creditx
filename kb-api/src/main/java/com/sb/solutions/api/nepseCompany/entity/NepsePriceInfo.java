package com.sb.solutions.api.nepseCompany.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NepsePriceInfo  extends BaseEntity<Long>{
    private Date sharePriceDate;
    private String avgDaysForPrice;


}
