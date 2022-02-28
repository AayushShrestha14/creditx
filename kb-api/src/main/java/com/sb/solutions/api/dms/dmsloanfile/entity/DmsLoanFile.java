package com.sb.solutions.api.dms.dmsloanfile.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Priority;
import com.sb.solutions.core.enums.Securities;
import com.sb.solutions.core.utils.NumberToWordsConverter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmsLoanFile extends BaseEntity<Long> {

    private static final Logger logger = LoggerFactory.getLogger(DmsLoanFile.class);

    private double interestRate;
    private BigDecimal proposedAmount;
    @Transient
    private String proposedAmountWord;
    private String serviceChargeType;
    private double serviceChargeAmount;
    @Column(columnDefinition = "text")
    private String documentPath;
    @Transient
    private List<String> documentMap;
    //    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<LoanDocument> documents;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Securities.class)
    private Set<Securities> securities;
    @Transient
    private List<Map<String, String>> documentPathMaps;
    private Date tenure;
    private int tenureDuration;
    private Priority priority;
    private String recommendationConclusion;
    private String waiver;
    private Double fmvTotal;
    private Double distressValue;
    private Double totalLoanLimit;
    private Double individualExposure;
    private Double institutionExposure;
    private Double groupExpo;
    private Double fmvFundingPercent;
    private Double incomeCoverageRatio;
    private Double debtServiceCoverageRatio;
    private String keyPersonName;
    private String dealingProductName;

    public List<Map<String, String>> getDocumentPathMaps() {
        String documentsPaths = null;
        Gson gson = new Gson();
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            documentsPaths = this.getDocumentPath();
            List<String> documentsPathList = gson.fromJson(documentsPaths, List.class);
            List<String> documentNames = new ArrayList<>();
            List<String> documentPaths = new ArrayList<>();
            int count = 0;
            if (CollectionUtils.isNotEmpty(documentsPathList)) {
                for (String list : documentsPathList) {
                    String[] arrayOfString = list.split(":");
                    documentNames.add(arrayOfString[0]);
                    documentPaths.add(arrayOfString[1]);
                }
            }
            for (String documentPath : documentPaths) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put(documentNames.get(count), documentPath);
                count++;
                mapList.add(map);
            }
            this.setDocumentPathMaps(mapList);
        } catch (Exception e) {
            logger.warn("unable to doc path {}", e);
        }

        return this.documentPathMaps;
    }

    public String getProposedAmountWord() {
        try {
            return NumberToWordsConverter
                .calculateAmountInWords(String.valueOf(this.getProposedAmount()));
        } catch (Exception e) {
            logger.warn("unable to convert {}", e);
            return null;
        }
    }


}
//    @PrePersist
//    public void prePersist() {
//        try {
//            this.setDocumentPath(new Gson().toJson(this.getDocumentMap()));
//            this.setCreatedAt(new Date());
//        } catch (Exception e) {
//            logger.warn("unable to set document path or created at", e);
//        }
//    }


