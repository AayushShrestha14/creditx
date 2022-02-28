package com.sb.solutions.api.approvallimit.emuns;

public enum Authorities {
    ROLE_AUTHENTICATED("Role Authenticated"),
    ROLE_ADMINISTRATOR("Role Administrator"),
    ROLE_RELATIONSHIP_MANAGER("Role Relationship Manager"),
    ROLE_SENIOR_RELATIONSHIP_MANAGER("Role Senior Relationship Manager"),
    ROLE_BRANCH_MANAGER("Role Branch Manager"),
    ROLE_REGIONAL_MANAGER("Role Regional Manager"),
    ROLE_CREDIT_RISK_OFFICER("Role Credit Risk Officer"),
    ROLE_RISK_MANAGER("Role Risk Manager"),
    ROLE_MANAGER("Role Manager"),
    ROLE_SENIOR_MANAGER("Role Senior Manager"),
    ROLE_ASSISTANT_GM("Role Assistant GM"),
    ROLE_DEPUTY_GM("Role Deputy GM"),
    ROLE_DEPUTY_CEO("Role Deputy CEO"),
    ROLE_CEO("Role CEO"),
    ROLE_BOD("Role BOD"),
    ROLE_CA("Roel CA"),
    ROLE_CREDIT_COMMITTEE("Role Credit Committee");
    public final String value;

    Authorities(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
