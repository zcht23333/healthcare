package com.zch.controller;

import com.zch.mapper.StatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private StatMapper statMapper;

    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> data = new HashMap<>();
        // 获取三个维度的统计数据
        data.put("totalCompany", statMapper.countCompanies());
        data.put("totalDrug", statMapper.countDrugs());
        data.put("pieData", statMapper.getDrugCountByCompany());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取大盘数据成功");
        result.put("data", data);

        return result;
    }
}