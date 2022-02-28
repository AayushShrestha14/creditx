package com.sb.solutions.api.accountCategory.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Where;

import com.sb.solutions.api.accountType.entity.AccountType;
import com.sb.solutions.api.document.entity.Document;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCategory {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private AccountType accountType;

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> documents;

    private String additionalInformation;
}
