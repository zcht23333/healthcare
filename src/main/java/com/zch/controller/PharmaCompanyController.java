package com.zch.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.entity.PharmaCompany;
import com.zch.mapper.PharmaCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pharmaCompany")
public class PharmaCompanyController {

    @Autowired
    private PharmaCompanyMapper pharmaCompanyMapper;

    /**
     * 分页查询医药公司列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String companyName) {

        // 1. 开启分页 (PageHelper 会自动拦截紧跟在它后面的第一条 MyBatis 查询，为其加上 LIMIT)
        PageHelper.startPage(pageNum, pageSize);

        // 2. 执行正常的查询 (看起来是查全部，但由于上面开启了分页，底层执行的是带 LIMIT 的 SQL)
        List<PharmaCompany> list = pharmaCompanyMapper.selectList(companyName);

        // 3. 将查询结果包装进 PageInfo 中，它会自动计算出总页数、总条数等丰富信息
        PageInfo<PharmaCompany> pageInfo = new PageInfo<>(list);

        // 4. 组装给前端的 JSON 返回值
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", pageInfo);

        return result;
    }

    /**
     * 新增医药公司
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody PharmaCompany pharmaCompany) {
        pharmaCompanyMapper.insert(pharmaCompany);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "新增医药公司成功！");
        return result;
    }

    /**
     * 修改医药公司
     */
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody PharmaCompany pharmaCompany) {
        pharmaCompanyMapper.update(pharmaCompany);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "修改医药公司成功！");
        return result;
    }

    /**
     * 删除医药公司 (逻辑删除)
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Long id) {
        pharmaCompanyMapper.deleteById(id);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "医药公司已删除！");
        return result;
    }
}