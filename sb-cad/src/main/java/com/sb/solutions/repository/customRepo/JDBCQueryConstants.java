package com.sb.solutions.repository.customRepo;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author : Rujan Maharjan on  9/17/2021
 **/
public class JDBCQueryConstants {

    private static final String AND = " AND ";


    public static final String UNASSIGNED_LOAN = "SELECT DISTINCT l.id as id,"
        + "                l.name as name,"
        + "                l.customer_type as customerType,"
        + "                l.associate_id as associateId,"
        + "                l.id_number as idNumber,"
        + "                l.id_reg_date as idRegDate,"
        + "                l.id_reg_place as idRegPlace,"
        + "                b.name as branchName,"
        + "                p.name as provinceName,"
        + "                b.id as branchId ,"
        + "                STRING_AGG(c.id, ',') AS customerLoanIds"
        + "                from customer_loan c"
        + "         join customer_info l on l.id = c.loan_holder_id"
        + "         join branch b on l.branch_id = b.id"
        + "         join province p on b.province_id = p.id WHERE 1=1";


    public static final String UNASSIGNED_LOAN_TOTAL_COUNT = "SELECT count(DISTINCT l.id)"
        + "                from customer_loan c"
        + "         join customer_info l on l.id = c.loan_holder_id"
        + "         join branch b on l.branch_id = b.id"
        + "         join province p on b.province_id = p.id WHERE 1=1 AND c.id not in (select a.assigned_loan_id from assigned_loan a) and c.document_status = 1 ";


    private static final String GROUP_BY =
        " AND c.id not in (select a.assigned_loan_id from assigned_loan a) and c.document_status = 1 group by l.id, l.name, l.associate_id,  l.customer_type, l.id_number, l.id_reg_date, l.id_reg_place,\n"
            + "         b.name, p.name, b.id order by l.name asc  ";


    public static String createQueryUnassignedLoan(Map<String, String> map, Pageable pageable) {
        StringBuilder stringBuilder = new StringBuilder(UNASSIGNED_LOAN).append(whereClause(map));
        stringBuilder.append(GROUP_BY);
        stringBuilder.append("offset ").append(pageable.getOffset()).append(" rows fetch next  ")
            .append(pageable.getPageSize()).append(" rows only; ");
        return stringBuilder.toString();
    }

    public static String createQueryUnassignedLoanTotalCount(Map<String, String> map) {
        return UNASSIGNED_LOAN_TOTAL_COUNT + whereClause(map);

    }

    private static String whereClause(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (map.containsKey("branchIds")) {
            List<String> branchIdList = Arrays.asList(map.get("branchIds").split(","));
            stringBuilder.append(AND).append("b.id in (");
            for (String i : branchIdList) {
                stringBuilder.append(Long.valueOf(i) + ",");
            }
            stringBuilder.append(0 + ")");
        }
        if (map.containsKey("clientType")) {
            stringBuilder.append(AND).append("l.client_type = ")
                .append(ClientType.valueOf(map.get("clientType")).ordinal());
        }
        if (map.containsKey("customerType")) {
            stringBuilder.append(AND).append("l.customer_type = ")
                .append(CustomerType.valueOf(map.get("customerType")).ordinal());

        }
        if (map.containsKey("name")) {
            stringBuilder.append(AND).append("lower(l.name) like lower('").append(map.get("name"))
                .append("%')");
        }
        return stringBuilder.toString();
    }

}
