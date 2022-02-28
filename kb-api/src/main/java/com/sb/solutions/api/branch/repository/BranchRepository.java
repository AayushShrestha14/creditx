package com.sb.solutions.api.branch.repository;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.core.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.branch.dto.BranchDto;
import com.sb.solutions.api.branch.entity.Branch;

/**
 * @author Rujan Maharjan on 2/13/2019
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long>,
    JpaSpecificationExecutor<Branch> {


    @Query(value = "select"
        + "  (select  count(id) from branch where status=1) active,"
        + " (select  count(id) from branch where status=0) inactive,"
        + " (select  count(id) from branch) branches", nativeQuery = true)
    Map<Object, Object> branchStatusCount();

    @Query(value = "select b from Branch b where b.name like concat(:name,'%')")
    Page<Branch> branchFilter(@Param("name") String name, Pageable pageable);

    @Query(value = "select b from Branch b where b.name like  concat(:name,'%')")
    List<Branch> branchCsvFilter(@Param("name") String name);

    @Query(value = "SELECT b FROM Branch b where b.id not in (:branchId)")
    List<Branch> getByIdNotIn(@Param("branchId") List<Long> branchId);

    @Query(value = "SELECT new com.sb.solutions.api.branch.dto.BranchDto(b.id,b.name) FROM Branch b ")
    List<BranchDto> getBranch();

    List<Branch> getAllByProvinceIdAndStatus(Long pId , Status status);

    List<Branch> getAllByProvinceInAndStatus(List<Province> provinces , Status status);

    Branch findByName( String name);

    @Query(value = "SELECT b.id FROM branch b ",nativeQuery = true)
    List<Long>  getAllIdOfBranch();

}
