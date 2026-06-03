package com.zch.mapper;

import com.zch.entity.CityInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface CityInfoMapper {
    @Select("<script>" +
            "SELECT * FROM city_info WHERE is_deleted = 0 " +
            "<if test='cityName != null and cityName != \"\"'> " +
            "AND city_name LIKE CONCAT('%', #{cityName}, '%') " +
            "</if> " +
            "ORDER BY id DESC" +
            "</script>")
    List<CityInfo> selectList(@Param("cityName") String cityName);

    @Insert("INSERT INTO city_info(city_name, province) VALUES(#{cityName}, #{province})")
    int insert(CityInfo cityInfo);

    @Update("UPDATE city_info SET city_name = #{cityName}, province = #{province} WHERE id = #{id}")
    int update(CityInfo cityInfo);

    @Update("UPDATE city_info SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}