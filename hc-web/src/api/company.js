// src/api/company.js
import request from '@/utils/request'

// 分页查询医药公司列表
export function getCompanyList(params) {
  return request({
    url: '/pharmaCompany/list',
    method: 'get',
    params: params // 包含 pageNum, pageSize, companyName
  })
}

// 医药公司 (新增代码)
export function addCompany(data) {
  return request({
    url: '/pharmaCompany/add',
    method: 'post',
    data: data
  })
}

// 修改医药公司
export function updateCompany(data) {
  return request({
    url: '/pharmaCompany/update',
    method: 'post',
    data: data
  })
}

// 删除医药公司
export function deleteCompany(id) {
  return request({
    url: `/pharmaCompany/delete/${id}`,
    method: 'post'
  })
}