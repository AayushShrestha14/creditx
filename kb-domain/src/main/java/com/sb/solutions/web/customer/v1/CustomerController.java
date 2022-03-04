package com.sb.solutions.web.customer.v1;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.service.CustomerPropertiesDataLoaderService;
import com.sb.solutions.api.customer.service.CustomerService;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.aop.CustomerActivityLog;
import com.sb.solutions.api.customerRelative.entity.CustomerRelative;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import org.springframework.validation.annotation.Validated;
import com.sb.solutions.core.validation.constraint.FileFormatValid;

@RestController
@Validated
@RequestMapping("/v1/customer")
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;
    private final UserService userService;
    private final CustomerPropertiesDataLoaderService customerPropertiesDataLoaderService;

    @Autowired
    public CustomerController(
        CustomerService customerService,
        UserService userService,
        CustomerPropertiesDataLoaderService customerPropertiesDataLoaderService) {
        this.customerService = customerService;
        this.userService = userService;
        this.customerPropertiesDataLoaderService = customerPropertiesDataLoaderService;
    }

    @CustomerActivityLog(Activity.CUSTOMER_UPDATE)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);

        if (null == savedCustomer) {
            logger.error("Error while saving customer {}", customer);
            return new RestResponseDto()
                .failureModel("Error occurred while saving Customer " + customer);
        }

        return new RestResponseDto().successModel(savedCustomer);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getPageable(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(customerService.findAllPageable(searchDto, PaginationUtils
                .pageable(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return new RestResponseDto().successModel(customerService.findOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Customer customer) {

        final Customer savedCustomer = customerService.save(customer);

        if (null == savedCustomer) {
            return new RestResponseDto()
                .failureModel("Error occurred while updating Customer " + customer);
        }

        return new RestResponseDto().successModel(savedCustomer);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(customerService.findAll());
    }

    @GetMapping("/citizenship-no")
    public ResponseEntity<?> getByCustomerCitizenShip(@RequestParam String citizenshipNumber) {
        return new RestResponseDto()
            .successModel(customerService.findCustomerByCitizenshipNumber(citizenshipNumber));
    }


    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadPhoto(
        @RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("citizenNumber") String citizenNumber,
        @RequestParam("customerName") String name
    ) {
        Preconditions.checkNotNull(citizenNumber.equals("null") ? null
                : (StringUtils.isEmpty(citizenNumber) ? null : citizenNumber),
            "Citizenship Number is required to upload file.");
        Preconditions.checkNotNull(name.equals("undefined") || name.equals("null") ? null
            : (StringUtils.isEmpty(name) ? null : name), "Customer Name "
            + "is required to upload file.");
        String uploadPath = new StringBuilder(UploadDir.initialDocument)

            .append("customers")
            .append("/")
            .append(name)
            .append("/")
            .append(citizenNumber)
            .append("/")
            .toString();

        logger.info("File Upload Path {}", uploadPath);
        return FileUploadUtils
            .uploadFile(multipartFile, uploadPath, "Profile-Pic");

    }

    @PostMapping("check")
    public ResponseEntity<?> searchByNameCitizenNoAndIssueDate(
        @RequestBody CustomerRelative customer) {
        logger.info("check kyc relative is customer or not{}", customer);
        return new RestResponseDto().successModel(customerService
            .findCustomerByCustomerNameAndCitizenshipNumberAndCitizenshipIssuedDate(
                customer.getCustomerRelativeName(), customer.getCitizenshipNumber(),
                customer.getCitizenshipIssuedDate()));
    }

    @GetMapping("/client-type")
    public ResponseEntity<?> getClientType() {

        return new RestResponseDto().successModel(ClientType.getClientType());
    }

    @GetMapping("/subsectors")
    public ResponseEntity<?> getSubsectors() {
        return new RestResponseDto()
            .successModel(customerPropertiesDataLoaderService.getSubSectors());
    }
}
