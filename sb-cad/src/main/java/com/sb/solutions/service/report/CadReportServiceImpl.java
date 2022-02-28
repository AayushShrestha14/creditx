package com.sb.solutions.service.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.dto.CadReportDto;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;
import com.sb.solutions.enums.CADDocAction;
import com.sb.solutions.enums.CadDocStatus;
import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.enums.ReportType;
import com.sb.solutions.report.core.factory.ReportFactory;
import com.sb.solutions.report.core.model.Report;
import com.sb.solutions.repository.CustomerCadRepository;
import com.sb.solutions.repository.specification.CustomerCadSpecBuilder;

/**
 * @author : Rujan Maharjan on  5/19/2021
 **/
@Service
@Slf4j
public class CadReportServiceImpl implements CadReportService {

    private final UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));

    private final EntityManager em;

    private final CustomerCadRepository customerCadRepository;

    public CadReportServiceImpl(UserService userService, EntityManager em,
        CustomerCadRepository customerCadRepository) {
        this.userService = userService;
        this.em = em;
        this.customerCadRepository = customerCadRepository;
    }

    @Override
    public String reportPath(Map<String, String> filterParams) {
        Report report = ReportFactory.getReport(populate(Optional.of(filterParams)));
        return getDownloadPath() + report.getFileName();
    }

    @Override
    public String title() {
        return "Form Report For CAD";
    }

    @Override
    public List<AbstractColumn> columns() {
        AbstractColumn columnBranchName = ColumnBuilder.getNew()
            .setColumnProperty("branchName", String.class.getName())
            .setTitle("Branch").setWidth(100)
            .build();

        AbstractColumn columnBranchProvinceName = ColumnBuilder.getNew()
            .setColumnProperty("provinceName", String.class.getName())
            .setTitle("Province").setWidth(100)
            .build();

        AbstractColumn columnCustomerName = ColumnBuilder.getNew()
            .setColumnProperty("customerName", String.class.getName())
            .setTitle("Name").setWidth(100)
            .build();

        AbstractColumn columnCustomerType = ColumnBuilder.getNew()
            .setColumnProperty("customerType", CustomerType.class.getName())
            .setTitle("Customer Type").setWidth(100)
            .build();

        AbstractColumn columnCadDocStatus = ColumnBuilder.getNew()
            .setColumnProperty("docStatus", CadDocStatus.class.getName())
            .setTitle("Status").setWidth(100)
            .build();

        AbstractColumn columnCurrentPossession = ColumnBuilder.getNew()
            .setColumnProperty("currentPossession", String.class.getName())
            .setTitle("Current Possession").setWidth(100)
            .build();

        AbstractColumn columnFromUser = ColumnBuilder.getNew()
            .setColumnProperty("fromUser", String.class.getName())
            .setTitle("From").setWidth(100)
            .build();
        AbstractColumn columnFromUserSentOn = ColumnBuilder.getNew()
            .setColumnProperty("lastModifiedAt", String.class.getName())
            .setTitle("Action on").setWidth(100)
            .build();
        AbstractColumn columnFromUserAction = ColumnBuilder.getNew()
            .setColumnProperty("docAction", CADDocAction.class.getName())
            .setTitle("Action").setWidth(100)
            .build();

        AbstractColumn totalLoan = ColumnBuilder.getNew()
            .setColumnProperty("totalLoan", Integer.class.getName())
            .setTitle("Loan Document").setWidth(100)
            .build();

        AbstractColumn columnLoans = ColumnBuilder.getNew()
            .setColumnProperty("loanNames", String.class.getName())
            .setTitle("Loans").setWidth(100)
            .build();
        AbstractColumn totalProposedAmount = ColumnBuilder.getNew()
            .setColumnProperty("totalProposal", BigDecimal.class.getName())
            .setTitle("Proposed Amount").setWidth(100)
            .build();

        AbstractColumn totalLifeSpan = ColumnBuilder.getNew()
            .setColumnProperty("totalLifeSpan", Long.class.getName())
            .setTitle("Total Days").setWidth(100)
            .build();

        return Arrays.asList(columnBranchName, columnBranchProvinceName, columnCustomerName,
            columnCustomerType, columnCadDocStatus, columnCurrentPossession,
            columnFromUser, columnFromUserAction, columnFromUserSentOn, totalLoan,
            columnLoans, totalProposedAmount);
    }

    @Override
    public ReportParam populate(Optional optional) {
        Map<String, String> filterParams = objectMapper.convertValue(optional.get(), Map.class);
        final User user = userService.getAuthenticatedUser();
        if (!user.getRole().getRoleType().equals(RoleType.CAD_SUPERVISOR)) {
            String branchAccess = userService.getRoleAccessFilterByBranch().stream()
                .map(Object::toString).collect(Collectors.joining(","));
            if (filterParams.containsKey("branchIds")) {
                branchAccess = filterParams.get("branchIds");
            }
            filterParams.put("branchIds", branchAccess);
        } else {
            String provienceList = user.getProvinces().stream().map(Province::getId)
                .map(Objects::toString).collect(Collectors.joining(","));
            filterParams.put("provinceIds", provienceList);

        }
        boolean filterByToUser = false;
        if (!filterParams.containsKey("isCadFile")) {
            filterByToUser = true;
        } else if (filterParams.get("isCadFile").equalsIgnoreCase("false")) {
            filterByToUser = true;

        }

        if (filterByToUser) {
            if (!(user.getRole().getRoleType().equals(RoleType.CAD_SUPERVISOR) || user.getRole()
                .getRoleType().equals(RoleType.CAD_ADMIN) || user.getRole()
                .getRoleType().equals(RoleType.CAD_LEGAL))) {
                filterParams.put("toUser", user.getId().toString());
            }
        }

        if ((user.getRole().getRoleType().equals(RoleType.CAD_LEGAL)) && filterByToUser) {
            filterParams.put("toRole", user.getRole().getId().toString());
        }
        final CustomerCadSpecBuilder customerCadSpecBuilder = new CustomerCadSpecBuilder(
            filterParams);
        final Specification<CustomerApprovedLoanCadDocumentation> specification = customerCadSpecBuilder
            .build();
        final List list = getReportDto(specification);
        return ReportParam.builder().reportName("CAD Report")
            .title(title())

            .subTitle(subTitle()).columns(columns()).data(list).reportType(ReportType.FORM_REPORT)
            .filePath(UploadDir.WINDOWS_PATH + getDownloadPath()).exportType(ExportType.XLS)
            .build();
    }

    public String getDownloadPath() {
        return new PathBuilder(UploadDir.initialDocument)
            .buildBuildFormDownloadPath("CAD");
    }


    private List<CadReportDto> getReportDto(
        Specification<CustomerApprovedLoanCadDocumentation> innerSpec) {
        List list = new ArrayList();
        List<CustomerApprovedLoanCadDocumentation> data = customerCadRepository.findAll(innerSpec);
        if (!data.isEmpty()) {
            data.forEach(cad -> {
                CadReportDto cadReportDto = new CadReportDto();
                cadReportDto.setBranchName(cad.getLoanHolder().getBranch().getName());
                cadReportDto
                    .setProvinceName(cad.getLoanHolder().getBranch().getProvince().getName());
                cadReportDto.setCustomerType(cad.getLoanHolder().getCustomerType());
                cadReportDto.setDocStatus(cad.getDocStatus());
                cadReportDto.setCustomerName(cad.getLoanHolder().getName());
                StringBuilder stringBuilder = new StringBuilder()
                    .append(cad.getCadCurrentStage().getToUser() == null ? ""
                        : cad.getCadCurrentStage().getToUser().getName()).append(" (")
                    .append(cad.getCadCurrentStage().getToRole().getRoleName())
                    .append(")");
                cadReportDto.setCurrentPossession(stringBuilder.toString());
                List<CustomerLoan> customerLoanList = cad.getAssignedLoan();
                String loans = customerLoanList.stream().map(CustomerLoan::getLoan)
                    .map(LoanConfig::getName)
                    .collect(Collectors.joining(", "));
                cadReportDto.setLoanNames(loans);
                BigDecimal amount = customerLoanList.stream().map(CustomerLoan::getProposal).map(
                    Proposal::getProposedLimit)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                cadReportDto.setTotalProposal(amount);
                cadReportDto.setTotalLoan(customerLoanList.size());
                StringBuilder fromDetail = new StringBuilder()
                    .append(cad.getCadCurrentStage().getFromUser() == null ? ""
                        : cad.getCadCurrentStage().getFromUser().getName()).append(" (")
                    .append(cad.getCadCurrentStage().getFromRole().getRoleName())
                    .append(")");
                cadReportDto.setFromUser(fromDetail.toString());
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy hh.mm aa");
                String strDate = formatter.format(cad.getCadCurrentStage().getLastModifiedAt());
                cadReportDto.setLastModifiedAt(strDate);
                cadReportDto.setDocAction(cad.getCadCurrentStage().getDocAction());
                cadReportDto.setTotalLifeSpan(
                    this.calculateCadTotalSpan(new Date(), cad.getCreatedAt()));
                list.add(cadReportDto);
            });
        }
        return list;
    }

    public long calculateCadTotalSpan(Date lastModifiedDate, Date createdLastDate) {
        int daysdiff = 0;
        Date lastModifiedAt = lastModifiedDate;
        Date createdLastAt = createdLastDate;

        long differenceInDate = lastModifiedAt.getTime() - createdLastAt.getTime();
        long diffInDays = TimeUnit.DAYS.convert(differenceInDate, TimeUnit.MILLISECONDS);
        daysdiff = (int) diffInDays;

        return daysdiff;

    }
}
