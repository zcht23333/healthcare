package com.zch.mapper;

import com.zch.entity.DoctorInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface DoctorInfoMapper {

    @Select("<script>" +
            "SELECT * FROM doctor_info WHERE is_deleted = 0 " +
            "<if test='realName != null and realName != \"\"'> " +
            "AND real_name LIKE CONCAT('%', #{realName}, '%') " +
            "</if> " +
            "ORDER BY id DESC" +
            "</script>")
    List<DoctorInfo> selectList(@Param("realName") String realName);

    // 新增时密码由 Controller 赋予默认值
    @Insert("INSERT INTO doctor_info(username, password, real_name, hospital_name, department, phone) " +
            "VALUES(#{username}, #{password}, #{realName}, #{hospitalName}, #{department}, #{phone})")
    int insert(DoctorInfo doctorInfo);

    // 修改基本信息 (不修改密码)
    @Update("UPDATE doctor_info SET username = #{username}, real_name = #{realName}, " +
            "hospital_name = #{hospitalName}, department = #{department}, phone = #{phone} " +
            "WHERE id = #{id}")
    int update(DoctorInfo doctorInfo);

    @Update("UPDATE doctor_info SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    // 特殊操作：单独重置密码
    @Update("UPDATE doctor_info SET password = #{password} WHERE id = #{id}")
    int resetPassword(@Param("id") Long id, @Param("password") String password);
}