package com.sb.solutions.api.branch.service;

import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.basehttp.BaseHttpService;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.branch.repository.BranchRepository;
import com.sb.solutions.api.branch.repository.specification.BranchSpecBuilder;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.csv.CsvReader;
import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.enums.ReportType;
import com.sb.solutions.report.core.factory.ReportFactory;
import com.sb.solutions.report.core.model.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author Rujan Maharjan on 2/13/2019
 */
@Service
public class BranchServiceImpl implements BranchService {

    private static final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    BaseHttpService baseHttpService;

    @Autowired
    UserService userService;

    @Override
    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    @Override
    public Branch findOne(Long id) {
        return branchRepository.getOne(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ADD BRANCH') or hasAuthority('EDIT BRANCH')")
    public Branch save(Branch branch) {
        branch.setLastModifiedAt(new Date());
        if (branch.getId() == null) {
            branch.setStatus(Status.ACTIVE);
        }
        if (branch.getId() != null) {
            return branchRepository.save(branch);
        }
        if (!ObjectUtils.isEmpty(branch.getName())){
            Branch sameBranchNameExist = this.branchRepository.findByName(branch.getName());
            if (sameBranchNameExist != null) {
                throw new ServiceValidationException("Branch name already exist.");
            }
        }
        branch.setBranchCode(branch.getBranchCode().toUpperCase());
        return branchRepository.save(branch);
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW BRANCH')")
    public Page<Branch> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(object, Map.class);
        final BranchSpecBuilder branchSpecBuilder = new BranchSpecBuilder(s);
        final Specification<Branch> specification = branchSpecBuilder.build();
        return branchRepository.findAll(specification, pageable);
    }

    @Override
    public List<Branch> saveAll(List<Branch> list) {
        return branchRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> branchStatusCount() {
        return branchRepository.branchStatusCount();
    }

    @Override
    @PreAuthorize("hasAuthority('DOWNLOAD CSV')")
    public String csv(Object searchDto) {
        Report report = ReportFactory.getReport(populate(Optional.of(searchDto)));
        return getDownloadPath() + report.getFileName();

    }

    @Override
    public void saveExcel(MultipartFile file) {
        CsvReader csvReader = new CsvReader();
        csvReader.excelReader(file);
    }

    @Override
    public List<Branch> getAccessBranchByCurrentUser() {
        if (userService.getAuthenticatedUser().getRole().getRoleAccess().equals(RoleAccess.ALL)) {
            List<Branch> list = branchRepository.findAll();
            if (list.size() > 0) {
                list.sort(Comparator.comparing(Branch::getName));
            }
            return list;
        }
        return userService.getAuthenticatedUser().getBranch();
    }

    @Override
    public List<Branch> getBranchNoTAssignUser(Long roleId) {
        List<User> userList = userService.findByRoleId(roleId);
        List<Long> branches = new ArrayList<>();
        for (User u : userList) {
            for (Branch b : u.getBranch()) {
                branches.add(b.getId());
            }
        }
        if (branches.isEmpty()) {
            return branchRepository.findAll();
        }
        return branchRepository.getByIdNotIn(branches);
    }

    @Override
    public List<Branch> getBranchByProvince(Long pId) {
        return branchRepository.getAllByProvinceIdAndStatus(pId , Status.ACTIVE);
    }

    @Override
    public List<Branch> getBranchByProvinceList(List<Province> provinces) {
        return branchRepository.getAllByProvinceInAndStatus(provinces , Status.ACTIVE);
    }

    @Override
    public String title() {
        return "Form Report For Branch";
    }

    @Override
    public List<AbstractColumn> columns() {
        AbstractColumn columnName = ColumnBuilder.getNew()
                .setColumnProperty("name", String.class.getName())
                .setTitle("Branch Name").setWidth(85)
                .build();

        AbstractColumn columnProvince = ColumnBuilder.getNew()
                .setColumnProperty("province.name", String.class.getName())
                .setTitle("Province").setWidth(85)
                .build();

        AbstractColumn columnDistrict = ColumnBuilder.getNew()
                .setColumnProperty("district.name", String.class.getName())
                .setTitle("District").setWidth(85)
                .build();
        AbstractColumn columnMunicipalityVdc = ColumnBuilder.getNew()
                .setColumnProperty("municipalityVdc.name", String.class.getName())
                .setTitle("Municipality / VDC").setWidth(85)
                .build();
        AbstractColumn columnStreetName = ColumnBuilder.getNew()
                .setColumnProperty("streetName", String.class.getName())
                .setTitle("Street Name").setWidth(85)
                .build();

        AbstractColumn columnWardNumber = ColumnBuilder.getNew()
                .setColumnProperty("wardNumber", String.class.getName())
                .setTitle("Ward No.").setWidth(85)
                .build();
        AbstractColumn columnEmail = ColumnBuilder.getNew()
                .setColumnProperty("email", String.class.getName())
                .setTitle("Email").setWidth(85)
                .build();
        AbstractColumn columnLandlineNumber = ColumnBuilder.getNew()
                .setColumnProperty("landlineNumber", String.class.getName())
                .setTitle("Land Line Number").setWidth(85)
                .build();


        return Arrays.asList(columnName, columnProvince, columnDistrict, columnMunicipalityVdc,
                columnStreetName, columnWardNumber, columnEmail, columnLandlineNumber);
    }

    @Override
    public ReportParam populate(Optional optional) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, String> s = objectMapper.convertValue(optional.get(), Map.class);
        final BranchSpecBuilder branchSpecBuilder = new BranchSpecBuilder(s);
        final Specification<Branch> specification = branchSpecBuilder.build();
        final List branchList = branchRepository.findAll(specification);

        String filePath = getDownloadPath();
        ReportParam reportParam = ReportParam.builder().reportName("Branch Report").title(title())
                .subTitle(subTitle()).columns(columns()).data(branchList).reportType(ReportType.FORM_REPORT)
                .filePath(UploadDir.WINDOWS_PATH + filePath).exportType(ExportType.XLS)
                .build();


        return reportParam;
    }

    public String getDownloadPath() {
        return new PathBuilder(UploadDir.initialDocument)
                .buildBuildFormDownloadPath("Branch");
    }
}
