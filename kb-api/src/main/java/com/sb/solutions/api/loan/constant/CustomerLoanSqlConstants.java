package com.sb.solutions.api.loan.constant;

/**
 * @author : Rujan Maharjan on  9/28/2020
 **/
public class CustomerLoanSqlConstants {

    public static final String GET_LOAN_BY_GROUP_ID_AND_NOT_IN_CURRENT_LOAN_HOLDER =
        "SELECT ci.id     "
            + "         as loanHolderId,"
            + "       ci.name            as customerName,"
            + "       cg.id as groupId,"
            + "       cg.group_code as groupCode,"
            + "       cg.group_limit as groupLimit,"
            + "       cl.id            as customerLoanId,"
            + "       p.proposed_limit as proposedLimit,"
            + "       cl.document_status as documentStatus"
            + "from customer_info ci"
            + "         join customer_group cg on cg.id = ci.customer_info_customer_group_id and cg.id = :gId"
            + "         join customer_loan cl on ci.id = cl.loan_holder_id  and cl.loan_holder_id <> :lId"
            + "         join proposal p on cl.proposal_id = p.id"
            + ""
            + ";";


    public static final String  GET_CAD_IN_USER_IDS = "SELECT "
        + "c.id             as id,"
        + "r2.role_name as roleName,"
        + "ci.name          as customerName,"
        + "lc.name          as facilityName,"
        + "p.proposed_limit as proposedAmount"
        + " FROM customer_approved_loan_cad_documentation c"
        + "         LEFT JOIN cad_stage cs on c.cad_current_stage_id = cs.id"
        + "         LEFT JOIN users u on cs.to_user_id = u.id"
        + "         LEFT JOIN role r2 on u.role_id = r2.id"
        + "         LEFT JOIN customer_info ci on c.loan_holder_id = ci.id"
        + "         LEFT JOIN assigned_loan al on c.id = al.customer_approved_loan_cad_documentation_id"
        + "         LEFT JOIN customer_loan cl on al.assigned_loan_id = cl.id"
        + "         LEFT JOIN proposal p on cl.proposal_id = p.id"
        + "         LEFT JOIN loan_config lc on lc.id = cl.loan_id"
        + "WHERE u.primary_user_id = :uId and r2.id in (:roleIDs)";

}
