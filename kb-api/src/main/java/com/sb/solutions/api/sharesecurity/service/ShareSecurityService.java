package com.sb.solutions.api.sharesecurity.service;

import java.util.Optional;

import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperService;
import com.sb.solutions.api.sharesecurity.ShareSecurity;
import com.sb.solutions.core.service.BaseService;

public interface ShareSecurityService extends BaseService<ShareSecurity>,
    HelperService<Optional<HelperDto<Long>>> {

}
