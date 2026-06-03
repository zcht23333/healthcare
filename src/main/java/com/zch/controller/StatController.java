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

        // 四大核心卡片数据
        data.put("totalCompany", statMapper.countCompanies());
        data.put("totalDrug", statMapper.countDrugs());
        data.put("totalDoctor", statMapper.countDoctors());
        data.put("totalPolicy", statMapper.countPolicies());

        // ECharts 双图表数据
        data.put("pieData", statMapper.getDrugCountByCompany());
        data.put("barData", statMapper.getDoctorCountByDept());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取大盘数据成功");
        result.put("data", data);

        return result;
    }
}