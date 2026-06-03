package com.zch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class MedInsurancePolicy {
    private Long id;
    private String policyTitle;
    private String policyContent; // 存放 WangEditor 生成的超长 HTML 富文本

    // 政策发布日期一般精确到天即可
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishDate;

    private Long applicableCityId; // 适用城市ID (预留字段，后续可关联城市表)
    private Integer isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // --- 请利用 IDEA 快捷键 (Alt+Insert) 生成所有属性的 Getter 和 Setter 方法 ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPolicyTitle() { return policyTitle; }
    public void setPolicyTitle(String policyTitle) { this.policyTitle = policyTitle; }
    public String getPolicyContent() { return policyContent; }
    public void setPolicyContent(String policyContent) { this.policyContent = policyContent; }
    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }
    public Long getApplicableCityId() { return applicableCityId; }
    public void setApplicableCityId(Long applicableCityId) { this.applicableCityId = applicableCityId; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}