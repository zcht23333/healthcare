package com.zch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class PharmaPolicy {
    private Long id;
    private Long companyId;
    private String policyTitle;
    private String policyContent; // 富文本内容

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    private Integer isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // --- 额外增加的业务字段，用于联表查询时接收公司名称 ---
    private String companyName;

    // 请用快捷键 (Alt+Insert) 生成所有的 Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getPolicyTitle() { return policyTitle; }
    public void setPolicyTitle(String policyTitle) { this.policyTitle = policyTitle; }
    public String getPolicyContent() { return policyContent; }
    public void setPolicyContent(String policyContent) { this.policyContent = policyContent; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}