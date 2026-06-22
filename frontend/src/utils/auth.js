const TOKEN_KEY = 'gcsj2_token'
const USER_KEY = 'gcsj2_user'

/**
 * 获取令牌
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * 设置令牌
 */
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 移除令牌
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

/**
 * 获取用户信息
 */
export function getUser() {
  const user = localStorage.getItem(USER_KEY)
  return user ? JSON.parse(user) : null
}

/**
 * 设置用户信息
 */
export function setUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

/**
 * 移除用户信息
 */
export function removeUser() {
  localStorage.removeItem(USER_KEY)
}

/**
 * 是否已登录
 */
export function isLoggedIn() {
  return !!getToken()
}

/**
 * 退出登录
 */
export function logout() {
  removeToken()
  removeUser()
}
