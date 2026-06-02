// src/api/login.js
import request from '@/utils/request'

// 获取图形验证码
export function fetchCaptcha() {
  return request({
    url: '/captcha',
    method: 'get'
  })
}

// 提交登录 (预留给下一步使用)
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}