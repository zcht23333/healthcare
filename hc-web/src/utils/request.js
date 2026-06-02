import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router' // 【重要】引入路由，用于被踢回登录页

const service = axios.create({
  baseURL: 'http://localhost:8080', 
  timeout: 5000 
})

// 1. 请求拦截器
service.interceptors.request.use(
  config => {
    // 【修复点1】真正从本地获取 Token，并塞入请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 2. 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      // 【修复点2】识别到 401 状态码，主动清除残留数据并跳转到登录页
      if (res.code === 401) {
        ElMessage.error('登录状态异常或已过期，请重新登录')
        localStorage.removeItem('token') 
        router.push('/login') // 踢回登录页
      } else {
        ElMessage.error(res.message || 'Error')
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res.data
    }
  },
  error => {
    ElMessage.error('网络请求异常，请检查后端是否启动！')
    return Promise.reject(error)
  }
)

export default service