package com.zch.mapper;

import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface StatMapper {

    @Select("SELECT COUNT(*) FROM pharma_company WHERE is_deleted = 0")
    int countCompanies();

    @Select("SELECT COUNT(*) FROM drug_info WHERE is_deleted = 0")
    int countDrugs();

    // 【新增】1. 统计注册医生总数
    @Select("SELECT COUNT(*) FROM doctor_info WHERE is_deleted = 0")
    int countDoctors();

    // 【新增】2. 统计发布的医保政策总数
    @Select("SELECT COUNT(*) FROM med_insurance_policy WHERE is_deleted = 0")
    int countPolicies();

    // 饼图：各公司药品数量
    @Select("SELECT c.company_name AS name, COUNT(d.id) AS value " +
            "FROM drug_info d " +
            "JOIN pharma_company c ON d.company_id = c.id " +
            "WHERE d.is_deleted = 0 AND c.is_deleted = 0 " +
            "GROUP BY c.company_name")
    List<Map<String, Object>> getDrugCountByCompany();

    // 【新增】3. 柱状图：统计各个科室下的医生人数分布
    @Select("SELECT department AS name, COUNT(id) AS value " +
            "FROM doctor_info WHERE is_deleted = 0 " +
            "GROUP BY department")
    List<Map<String, Object>> getDoctorCountByDept();
}