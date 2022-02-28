package com.sb.solutions.api.productMode.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enums.Product;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 6/24/2019
 */
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductMode {

    @Id
    private Long id;

    private Product product;

    private Status status;
}
