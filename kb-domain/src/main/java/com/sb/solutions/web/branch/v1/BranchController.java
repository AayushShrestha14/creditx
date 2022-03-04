package com.sb.solutions.web.branch.v1;

import java.util.List;
import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.branch.service.BranchService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.web.branch.v1.dto.BranchCustomerEndDto;
import com.sb.solutions.web.branch.v1.mapper.BranchCustomerEndMapper;

/**
 * @author Rujan Maharjan on 2/13/2019
 */

@RestController
@Validated
@RequestMapping("/v1/branch")
public class BranchController {

    private final BranchService branchService;
    private final BranchCustomerEndMapper branchCustomerEndMapper;

    public BranchController(
        @Autowired BranchService branchService,
        @Autowired BranchCustomerEndMapper branchCustomerEndMapper
    ) {
        this.branchService = branchService;
        this.branchCustomerEndMapper = branchCustomerEndMapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveBranch(@Valid @RequestBody Branch branch) {
        final Branch b = branchService.save(branch);

        if (b == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            return new RestResponseDto().successModel(b);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResponseEntity<?> uploadBranchExcel(@RequestParam("file") @FileFormatValid MultipartFile multipartFile) {
        branchService.saveExcel(multipartFile);
        return new RestResponseDto().successModel(null);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @RequestMapping(method = RequestMethod.POST, path = "/list")
    public ResponseEntity<?> getPageableBranch(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            branchService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/statusCount")
    public ResponseEntity<?> getBranchStatusCount() {
        return new RestResponseDto().successModel(branchService.branchStatusCount());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getBranch() {
        return new RestResponseDto().successModel(branchService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/csv")
    public ResponseEntity<?> csv(@RequestBody Object searchDto) {
        return new RestResponseDto().successModel((branchService.csv(searchDto)));
    }

    @GetMapping(value = "/branch-current-user")
    public ResponseEntity<?> getBranchAccessByCurrentUser() {
        return new RestResponseDto().successModel(branchService.getAccessBranchByCurrentUser());
    }

    @GetMapping(value = "/{id}/unique")
    public ResponseEntity<?> getBranchNoTAssignUser(@PathVariable Long id) {
        return new RestResponseDto().successModel(branchService.getBranchNoTAssignUser(id));
    }

    @GetMapping(value = "/limited")
    public ResponseEntity<?> getBranchWithLimitedInfo() {
        List<BranchCustomerEndDto> branchDtoList = branchCustomerEndMapper
            .mapEntitiesToDtos(branchService.findAll());
        return new RestResponseDto().successModel(branchDtoList);
    }

    @GetMapping(value = "/province/{id}")
    public ResponseEntity<?> getBranchByProvince(Long id) {
        List<BranchCustomerEndDto> branchDtoList = branchCustomerEndMapper
            .mapEntitiesToDtos(branchService.getBranchByProvince(id));
        return new RestResponseDto().successModel(branchDtoList);
    }

    @PostMapping(value = "/provinceList")
    public ResponseEntity<?> getBranchByProvinceList(@RequestBody List<Province> provinces) {
        List<Branch> branchList = branchService.getBranchByProvinceList(provinces);
        return new RestResponseDto().successModel(branchList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAuthenticated(@PathVariable Long id) {
        return new RestResponseDto().successModel(branchService.findOne(id));
    }
}
