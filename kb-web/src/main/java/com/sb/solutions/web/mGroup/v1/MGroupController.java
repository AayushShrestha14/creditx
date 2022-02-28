package com.sb.solutions.web.mGroup.v1;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.service.CustomerInfoService;
import com.sb.solutions.api.mGroupInfo.entity.MGroupInfo;
import com.sb.solutions.api.mGroupInfo.service.MGroupService;
import com.sb.solutions.core.controller.BaseController;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.web.mGroup.v1.dto.MGroupDto;
import com.sb.solutions.web.mGroup.v1.mapper.MGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(MGroupController.URL)
public class MGroupController extends BaseController<MGroupInfo, MGroupDto, Long> {
    static final String URL = "/v1/m-group";
    private static final Logger logger = LoggerFactory.getLogger(MGroupController.class);
    private final MGroupService service;
    private final MGroupMapper mapper;
    private CustomerInfoService customerInfoService;

    public MGroupController(MGroupService service, @Autowired MGroupMapper mGroupMapper, CustomerInfoService customerInfoService) {
        super(service, mGroupMapper);
        this.service = service;
        this.mapper = mGroupMapper;
        this.customerInfoService = customerInfoService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody MGroupDto dto) {

        final MGroupInfo mGroupInfo = service.save(mapper.mapDtoToEntity(dto));
        Optional<CustomerInfo> customerInfo = customerInfoService.findOne(dto.getCustomerInfoId());
        if(customerInfo.isPresent()){
            customerInfo.get().setMGroupInfo(mGroupInfo);
            customerInfoService.save(customerInfo.get());
        } else {
            logger.error("No Customer Found to associate group {}", dto);
            return new RestResponseDto()
                    .failureModel("No Customer Found " + dto);
        }
        if (null == mGroupInfo) {
            logger.error("Error while saving Group {}", dto);
            return new RestResponseDto()
                    .failureModel("Error occurred while saving " + dto);
        }
        return new RestResponseDto().successModel(mapper.mapEntityToDto(mGroupInfo));
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
