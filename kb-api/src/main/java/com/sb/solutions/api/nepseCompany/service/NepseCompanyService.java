package com.sb.solutions.api.nepseCompany.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.api.nepseCompany.entity.NepsePriceInfo;
import com.sb.solutions.core.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

public interface NepseCompanyService extends BaseService<NepseCompany> {

    Map<Object, Object> nepseStatusCount();

    void saveList(List<NepseCompany> nepseCompanyList);

    List<NepseCompany> getAllNepseBySearchDto(Object searchDto);

    void uploadNepseData(MultipartFile file, NepsePriceInfo nepsePriceInfo);

}
