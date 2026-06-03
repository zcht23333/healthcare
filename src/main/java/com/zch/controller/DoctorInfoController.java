package com.zch.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.entity.DoctorInfo;
import com.zch.mapper.DoctorInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorInfoController {

    @Autowired
    private DoctorInfoMapper doctorInfoMapper;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String realName) {
        PageHelper.startPage(pageNum, pageSize);
        List<DoctorInfo> list = doctorInfoMapper.selectList(realName);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", new PageInfo<>(list));
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody DoctorInfo doctorInfo) {
        // 新建账号强制设置初始密码
        doctorInfo.setPassword("123456");
        doctorInfoMapper.insert(doctorInfo);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "新增成功，默认密码为123456"); return res;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody DoctorInfo doctorInfo) {
        doctorInfoMapper.update(doctorInfo);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "修改成功"); return res;
    }

    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Long id) {
        doctorInfoMapper.deleteById(id);
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "删除成功"); return res;
    }

    @PostMapping("/resetPwd/{id}")
    public Map<String, Object> resetPwd(@PathVariable("id") Long id) {
        // 重置为默认密码
        doctorInfoMapper.resetPassword(id, "123456");
        Map<String, Object> res = new HashMap<>(); res.put("code", 200); res.put("message", "密码已重置为 123456"); return res;
    }
}