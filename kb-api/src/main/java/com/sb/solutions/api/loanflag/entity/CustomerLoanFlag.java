package com.sb.solutions.api.loanflag.entity;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.core.enums.LoanFlag;

/**
 * Note: If the flag is loan specific, example: zero proposed amount or insufficient considered
 * amount for share, then {@link CustomerLoanFlag#customerLoanId} is required to be set when
 * creating a new flag. Otherwise, it is good left `null`.
 *
 * @author Elvin Shrestha on 4/28/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerLoanFlag extends AbstractPersistable<Long> {

    private LoanFlag flag;

    private String description;

    @Column(name = "flag_order")
    private int order;

    private Boolean notifiedByEmail;

    private Long customerLoanId;

    @ManyToOne
    @JsonBackReference
    private CustomerInfo customerInfo;

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
                if (list == null || list.isEmpty()) {
                    return null;
                }
                if (list.size() > 1) {
                    List<CustomerLoanFlag> listDto = (List<CustomerLoanFlag>) list;
                    throw new IllegalStateException(listDto.get(0).description);
                }
                return list.get(0);
            }
        );
    }
}
