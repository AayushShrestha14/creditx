package com.sb.solutions.api.postApprovalDocument.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.sb.solutions.api.postApprovalDocument.enums.PostApprovalDocType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferLetter {

    @Id
    @GeneratedValue
    private Long id;
    private @NotNull String name;
    private @NotNull String templateUrl;

    private PostApprovalDocType postApprovalDocType;
}
