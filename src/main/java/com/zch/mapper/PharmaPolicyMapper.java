package com.zch.mapper;

import com.zch.entity.PharmaPolicy;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface PharmaPolicyMapper {

    // 联表查询：关联 pharma_company 获取 company_name
    @Select("<script>" +
            "SELECT p.*, c.company_name AS companyName " +
            "FROM pharma_policy p " +
            "LEFT JOIN pharma_company c ON p.company_id = c.id " +
            "WHERE p.is_deleted = 0 " +
            "<if test='policyTitle != null and policyTitle != \"\"'> " +
            "AND p.policy_title LIKE CONCAT('%', #{policyTitle}, '%') " +
            "</if> " +
            "ORDER BY p.id DESC" +
            "</script>")
    List<PharmaPolicy> selectList(@Param("policyTitle") String policyTitle);

    @Insert("INSERT INTO pharma_policy(company_id, policy_title, policy_content, start_date, end_date) " +
            "VALUES(#{companyId}, #{policyTitle}, #{policyContent}, #{startDate}, #{endDate})")
    int insert(PharmaPolicy pharmaPolicy);

    @Update("UPDATE pharma_policy SET company_id = #{companyId}, policy_title = #{policyTitle}, " +
            "policy_content = #{policyContent}, start_date = #{startDate}, end_date = #{endDate} " +
            "WHERE id = #{id}")
    int update(PharmaPolicy pharmaPolicy);

    @Update("UPDATE pharma_policy SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}