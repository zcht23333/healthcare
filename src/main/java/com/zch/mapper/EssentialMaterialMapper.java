package com.zch.mapper;

import com.zch.entity.EssentialMaterial;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface EssentialMaterialMapper {

    @Select("<script>" +
            "SELECT * FROM essential_material WHERE is_deleted = 0 " +
            "<if test='materialName != null and materialName != \"\"'> " +
            "AND material_name LIKE CONCAT('%', #{materialName}, '%') " +
            "</if> " +
            "ORDER BY id DESC" +
            "</script>")
    List<EssentialMaterial> selectList(@Param("materialName") String materialName);

    @Insert("INSERT INTO essential_material(material_name, material_type, description) " +
            "VALUES(#{materialName}, #{materialType}, #{description})")
    int insert(EssentialMaterial material);

    @Update("UPDATE essential_material SET material_name = #{materialName}, " +
            "material_type = #{materialType}, description = #{description} WHERE id = #{id}")
    int update(EssentialMaterial material);

    @Update("UPDATE essential_material SET is_deleted = 1 WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}