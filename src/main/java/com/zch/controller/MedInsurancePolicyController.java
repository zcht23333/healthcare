package com.zch.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.entity.MedInsurancePolicy;
import com.zch.mapper.MedInsurancePolicyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/policy")
public class MedInsurancePolicyController {

    @Autowired
    private MedInsurancePolicyMapper policyMapper;

    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String policyTitle) {

        PageHelper.startPage(pageNum, pageSize);
        List<MedInsurancePolicy> list = policyMapper.selectList(policyTitle);
        PageInfo<MedInsurancePolicy> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", pageInfo);
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody MedInsurancePolicy policy) {
        policyMapper.insert(policy);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "新增政策成功");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody MedInsurancePolicy policy) {
        policyMapper.update(policy);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "修改政策成功");
        return result;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Long id) {
        policyMapper.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "政策已删除");
        return result;
    }
}