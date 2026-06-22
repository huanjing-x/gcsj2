import request from '@/utils/request'

/**
 * 获取当前用户所有配置
 */
export function getConfigApi() {
  return request.get('/config')
}

/**
 * 保存配置
 */
export function saveConfigApi(data) {
  return request.put('/config', data)
}
