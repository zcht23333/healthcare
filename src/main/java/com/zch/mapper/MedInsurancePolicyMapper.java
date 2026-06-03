package com.zch.mapper;

import com.zch.entity.MedInsurancePolicy;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MedInsurancePolicyMapper {

    /**
     * 分页多条件查询（目前支持根据标题模糊搜索）
     */
    @Select("<script>" +
            "SELECT * FROM med_insurance_policy " +
            "WHERE is_deleted = 0 " +
            "<if test='policyTitle != null and policyTitle != \"\"'> " +
            "AND policy_title LIKE CONCAT('%', #{policyTitle}, '%') " +
            "</if> " +
            "ORDER BY publish_date DESC, id DESC" + // 按照发布时间和ID倒序排列
            "</script>")
    List<MedInsurancePolicy> selectList(@Param("policyTitle") String policyTitle);

    @Insert("INSERT INTO med_insurance_policy(policy_title, policy_content, publish_date, applicable_city_id) " +
            "VALUES(#{policyTitle}, #{policyContent}, #{publishDate}, #{applicableCityId})")
    int insert(MedInsurancePolicy policy);

    @Update("UPDATE med_insurance_policy SET policy_title = #{policyTitle}, policy_content = #{policyContent}, " +
            "publish_date = #{publishDate}, applicable_city_id = #{applicableCityId} " +
            "WHERE id = #{id}")
    int update(MedInsurancePolicy policy);

    // 逻辑删除
    @Update("UPDATE med_insurance_policy SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}