// src/api/test.js
import request from '@/utils/request'

// 测试连通性的接口
export function fetchHello() {
  return request({
    url: '/test',
    method: 'get'
  })
}