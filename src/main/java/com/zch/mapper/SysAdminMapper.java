package com.zch.mapper;

import com.zch.entity.SysAdmin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysAdminMapper {

    // 根据用户名和密码查询管理员
    @Select("SELECT * FROM sys_admin WHERE username = #{username} AND password = #{password}")
    SysAdmin loginCheck(@Param("username") String username, @Param("password") String password);

}