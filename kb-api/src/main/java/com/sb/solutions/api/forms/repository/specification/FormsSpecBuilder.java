package com.sb.solutions.api.forms.repository.specification;

import com.sb.solutions.api.forms.entity.Forms;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

/**
 * @author : Rujan Maharjan on  11/7/2020
 **/
public class FormsSpecBuilder extends BaseSpecBuilder<Forms> {

    public FormsSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<Forms> getSpecification(String property, String filterValue) {
        return new FormsSpec(property, filterValue);
    }
}
