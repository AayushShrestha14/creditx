package com.sb.solutions.api.approvallimit.repository.specification;

import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.repository.specification.UserSpec;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApprovalLimitSpecBuilder {

    private Map<String, String> params;

    public ApprovalLimitSpecBuilder(Map<String, String> params){
        this.params = params;
    }

    public Specification<ApprovalLimit> build(){
        if(MapUtils.isEmpty(params)){
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);


        Specification<ApprovalLimit> spec = new ApprovalLimitSpec(properties.get(0),
                params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++){
            final String property = properties.get(i);
            spec = Specification.where(spec)
                    .and(new ApprovalLimitSpec(property, params.get(property)));
        }


        return spec;
    }
}
