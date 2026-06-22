import request from '@/utils/request'

/**
 * 登录
 */
export function loginApi(data) {
  return request.post('/user/login', data)
}

/**
 * 注册
 */
export function registerApi(data) {
  return request.post('/user/register', data)
}

/**
 * 获取当前用户信息
 */
export function getUserInfoApi() {
  return request.get('/user/info')
}

/**
 * 修改昵称
 */
export function updateNicknameApi(nickName) {
  return request.put('/user/nickname', { nickName })
}

/**
 * 修改密码
 */
export function updatePasswordApi(data) {
  return request.put('/user/password', data)
}
