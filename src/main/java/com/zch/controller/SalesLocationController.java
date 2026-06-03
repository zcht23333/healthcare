package com.zch.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.entity.SalesLocation;
import com.zch.mapper.SalesLocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/location")
public class SalesLocationController {

    @Autowired
    private SalesLocationMapper salesLocationMapper;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String locationName) {
        PageHelper.startPage(pageNum, pageSize);
        List<SalesLocation> list = salesLocationMapper.selectList(locationName);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", new PageInfo<>(list));
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody SalesLocation location) {
        salesLocationMapper.insert(location);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "新增成功"); return res;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody SalesLocation location) {
        salesLocationMapper.update(location);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "修改成功"); return res;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Long id) {
        salesLocationMapper.deleteById(id);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "删除成功"); return res;
    }
}