package com.sb.solutions.api.openingForm.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sb.solutions.api.openingForm.entity.OpeningForm;

public interface OpeningFormRepository extends JpaRepository<OpeningForm, Long>,
    JpaSpecificationExecutor<OpeningForm> {

    @Query(value = "select (select count(id)"
        + "        from opening_form"
        + "        where status = 0"
        + "          and opening_form.branch_id in (:branchIds)) newed,"
        + "       (select count(id)"
        + "        from opening_form"
        + "        where status = 1"
        + "          and opening_form.branch_id in (:branchIds)) approval,"
        + "       (select count(id)"
        + "        from opening_form"
        + "        where status = 2"
        + "          and opening_form.branch_id in (:branchIds)) rejected,"
        + "       (select count(id)"
        + "        from opening_form"
        + "        where opening_form.branch_id in (:branchIds)) total", nativeQuery = true)
    Map<Object, Object> openingFormStatusCount(List<Long> branchIds);

    @Query(value = "select u.email from users u join role_permission_rights r on u.role_id=r.role_id join users_branch ub on u.id = ub.user_id  where r.permission_id=:permission and ub.branch_id=:branchId", nativeQuery = true)
    List<String> getUsersEmailHavingAccountOpeningPermissionInBranch(int permission, long branchId);
}
