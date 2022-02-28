package com.sb.solutions.api.productMode.service;

import com.sb.solutions.api.productMode.entity.ProductMode;
import com.sb.solutions.core.enums.Product;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.service.BaseService;

/**
 * @author Rujan Maharjan on 6/24/2019
 */
public interface ProductModeService extends BaseService<ProductMode> {

    ProductMode getByProduct(Product product, Status status);
}
