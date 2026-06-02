package com.zch.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.entity.DrugInfo;
import com.zch.mapper.DrugInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drugInfo")
public class DrugInfoController {

    @Autowired
    private DrugInfoMapper drugInfoMapper;

    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String drugName) {
        PageHelper.startPage(pageNum, pageSize);
        List<DrugInfo> list = drugInfoMapper.selectList(drugName);
        PageInfo<DrugInfo> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", pageInfo);
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody DrugInfo drugInfo) {
        drugInfoMapper.insert(drugInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "新增药品成功");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody DrugInfo drugInfo) {
        drugInfoMapper.update(drugInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "修改药品成功");
        return result;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Long id) {
        drugInfoMapper.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "药品已删除");
        return result;
    }
}