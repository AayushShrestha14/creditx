package com.sb.solutions.api.collateralSiteVisit.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.solutions.api.collateralSiteVisit.entity.SiteVisitDocument;
import lombok.Data;
import java.util.Date;
import java.util.List;
/**
 * Created by Karma Lama on October, 2021
 */
@Data
public class CollateralSiteVisitDto {

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date siteVisitDate;
    private String securityName;
    private String siteVisitJsonData;
    private List<SiteVisitDocument> siteVisitDocuments;
}
