package com.zch.mapper;

import com.zch.entity.SalesLocation;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SalesLocationMapper {

    @Select("<script>" +
            "SELECT s.*, c.city_name AS cityName " +
            "FROM sales_location s " +
            "LEFT JOIN city_info c ON s.city_id = c.id " +
            "WHERE s.is_deleted = 0 " +
            "<if test='locationName != null and locationName != \"\"'> " +
            "AND s.location_name LIKE CONCAT('%', #{locationName}, '%') " +
            "</if> " +
            "ORDER BY s.id DESC" +
            "</script>")
    List<SalesLocation> selectList(@Param("locationName") String locationName);

    @Insert("INSERT INTO sales_location(location_name, city_id, address, contact_person, contact_phone) " +
            "VALUES(#{locationName}, #{cityId}, #{address}, #{contactPerson}, #{contactPhone})")
    int insert(SalesLocation location);

    @Update("UPDATE sales_location SET location_name = #{locationName}, city_id = #{cityId}, " +
            "address = #{address}, contact_person = #{contactPerson}, contact_phone = #{contactPhone} " +
            "WHERE id = #{id}")
    int update(SalesLocation location);

    @Update("UPDATE sales_location SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}