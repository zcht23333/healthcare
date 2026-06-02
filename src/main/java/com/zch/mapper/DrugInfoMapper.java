package com.zch.mapper;

import com.zch.entity.DrugInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DrugInfoMapper {

    /**
     * 联表分页模糊查询
     */
    @Select("<script>" +
            "SELECT d.*, c.company_name AS companyName " +
            "FROM drug_info d " +
            "LEFT JOIN pharma_company c ON d.company_id = c.id " +
            "WHERE d.is_deleted = 0 " +
            "<if test='drugName != null and drugName != \"\"'> " +
            "AND d.drug_name LIKE CONCAT('%', #{drugName}, '%') " +
            "</if> " +
            "ORDER BY d.id DESC" +
            "</script>")
    List<DrugInfo> selectList(@Param("drugName") String drugName);

    @Insert("INSERT INTO drug_info(drug_name, company_id, approval_number, specification, unit_price, drug_image) " +
            "VALUES(#{drugName}, #{companyId}, #{approvalNumber}, #{specification}, #{unitPrice}, #{drugImage})")
    int insert(DrugInfo drugInfo);

    @Update("UPDATE drug_info SET drug_name = #{drugName}, company_id = #{companyId}, " +
            "approval_number = #{approvalNumber}, specification = #{specification}, unit_price = #{unitPrice}, drug_image = #{drugImage} " +
            "WHERE id = #{id}")
    int update(DrugInfo drugInfo);

    @Update("UPDATE drug_info SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

}