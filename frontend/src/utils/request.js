import axios from 'axios'
import { getToken, removeToken, removeUser } from './auth'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 Axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器 - 附加 JWT 令牌
request.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 统一处理错误
request.interceptors.response.use(
  response => {
    const res = response.data
    // 如果是文件下载等特殊请求，直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    // 业务成功
    if (res.code === 200) {
      return res
    }
    // 未登录或 token 失效
    if (res.code === 401) {
      ElMessage.error(res.msg || '登录已过期，请重新登录')
      removeToken()
      removeUser()
      router.push('/login')
      return Promise.reject(new Error(res.msg))
    }
    // 其他业务错误
    ElMessage.error(res.msg || '操作失败')
    return Promise.reject(new Error(res.msg))
  },
  error => {
    // HTTP 错误（网络异常、超时等）
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        removeToken()
        removeUser()
        router.push('/login')
      } else if (status === 500) {
        ElMessage.error('服务器内部错误')
      } else {
        ElMessage.error(`请求失败（${status}）`)
      }
    } else {
      ElMessage.error('网络异常，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request
