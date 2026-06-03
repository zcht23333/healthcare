package com.zch.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.entity.EssentialMaterial;
import com.zch.mapper.EssentialMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/material")
public class EssentialMaterialController {

    @Autowired
    private EssentialMaterialMapper materialMapper;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String materialName) {
        PageHelper.startPage(pageNum, pageSize);
        List<EssentialMaterial> list = materialMapper.selectList(materialName);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", new PageInfo<>(list));
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody EssentialMaterial material) {
        materialMapper.insert(material);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "新增成功"); return res;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody EssentialMaterial material) {
        materialMapper.update(material);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "修改成功"); return res;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Long id) {
        materialMapper.deleteById(id);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "删除成功"); return res;
    }
}