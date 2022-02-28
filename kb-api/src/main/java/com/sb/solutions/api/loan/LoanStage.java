package com.sb.solutions.api.loan;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sb.solutions.api.stage.entity.Stage;

@Entity
@Data
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@Audited
public class LoanStage extends Stage {

}
