// src/utils/request.js
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 1. 创建 axios 实例
const service = axios.create({
  // 注意：如果你的 Tomcat 配置了 /healthcare 虚拟路径，请改为 http://localhost:8080/healthcare
  baseURL: 'http://localhost:8080', 
  timeout: 5000 // 请求超时时间
})

// 2. 请求拦截器 (Request Interceptor)
service.interceptors.request.use(
  config => {
    // 以后在这里可以统一往请求头里塞 Token
    // const token = localStorage.getItem('token')
    // if (token) config.headers['Authorization'] = token
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 3. 响应拦截器 (Response Interceptor)
service.interceptors.response.use(
  response => {
    const res = response.data
    // 如果后端返回的自定义状态码不是 200，说明业务逻辑有错
    if (res.code !== 200) {
      ElMessage.error(res.message || 'Error')
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      // 一切正常，直接把 data 剥离出来返回给页面
      return res.data
    }
  },
  error => {
    ElMessage.error('网络请求异常，请检查后端是否启动！')
    return Promise.reject(error)
  }
)

export default service