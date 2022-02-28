package com.sb.solutions.api.productMode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.productMode.entity.ProductMode;
import com.sb.solutions.core.enums.Product;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 6/24/2019
 */
public interface ProductModeRepository extends JpaRepository<ProductMode, Long> {

    ProductMode getByProductAndStatus(Product product, Status status);
}
