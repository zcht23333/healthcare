package com.zch.mapper;

import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface StatMapper {

    // 1. 统计未删除的医药公司总数
    @Select("SELECT COUNT(*) FROM pharma_company WHERE is_deleted = 0")
    int countCompanies();

    // 2. 统计未删除的药品总数
    @Select("SELECT COUNT(*) FROM drug_info WHERE is_deleted = 0")
    int countDrugs();

    // 3. 统计各家公司的药品数量分布 (返回供 ECharts 饼图使用的 name 和 value 格式)
    @Select("SELECT c.company_name AS name, COUNT(d.id) AS value " +
            "FROM drug_info d " +
            "JOIN pharma_company c ON d.company_id = c.id " +
            "WHERE d.is_deleted = 0 AND c.is_deleted = 0 " +
            "GROUP BY c.company_name")
    List<Map<String, Object>> getDrugCountByCompany();
}