package com.sb.solutions.api.basehttp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Rujan Maharjan on 2/26/2019.
 */

@Service
public class BaseHttpService {

    @Autowired
    BaseHttpRepo baseHttpRepo;

    public String getBaseUrl() {
        return baseHttpRepo.baseUrl();

    }

}
