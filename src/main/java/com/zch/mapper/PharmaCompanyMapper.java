package com.zch.mapper;

import com.zch.entity.PharmaCompany;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PharmaCompanyMapper {

    /**
     * 根据医药公司名称模糊查询列表 (只查询未被逻辑删除的数据)
     */
    @Select("<script>" +
            "SELECT * FROM pharma_company " +
            "<where> " +
            "is_deleted = 0 " +
            "<if test='companyName != null and companyName != \"\"'> " +
            "AND company_name LIKE CONCAT('%', #{companyName}, '%') " +
            "</if> " +
            "</where> " +
            "ORDER BY id DESC" +
            "</script>")
    List<PharmaCompany> selectList(@Param("companyName") String companyName);

    /**
     * 新增医药公司
     */
    @Insert("INSERT INTO pharma_company(company_name, license_no, contact_person, contact_phone, address) " +
            "VALUES(#{companyName}, #{licenseNo}, #{contactPerson}, #{contactPhone}, #{address})")
    int insert(PharmaCompany pharmaCompany);

    /**
     * 修改医药公司信息
     */
    @Update("UPDATE pharma_company SET company_name = #{companyName}, license_no = #{licenseNo}, " +
            "contact_person = #{contactPerson}, contact_phone = #{contactPhone}, address = #{address} " +
            "WHERE id = #{id}")
    int update(PharmaCompany pharmaCompany);

    /**
     * 逻辑删除医药公司 (将 is_deleted 标记为 1)
     */
    @Update("UPDATE pharma_company SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

}