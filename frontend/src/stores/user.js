import { defineStore } from 'pinia'
import { ref } from 'vue'
import { setToken, setUser, removeToken, removeUser, getToken, getUser } from '@/utils/auth'
import { loginApi, registerApi, getUserInfoApi, updateNicknameApi, updatePasswordApi } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken() || '')
  const userInfo = ref(getUser() || null)

  /**
   * 登录
   */
  async function login(loginForm) {
    const res = await loginApi(loginForm)
    token.value = res.data.token
    setToken(res.data.token)
    const user = {
      userId: res.data.userId,
      username: res.data.username,
      nickName: res.data.nickName
    }
    userInfo.value = user
    setUser(user)
  }

  /**
   * 注册
   */
  async function register(registerForm) {
    await registerApi(registerForm)
  }

  /**
   * 获取用户信息
   */
  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res.data
    setUser(res.data)
  }

  /**
   * 修改昵称
   */
  async function updateNickname(nickName) {
    await updateNicknameApi(nickName)
    if (userInfo.value) {
      userInfo.value.nickName = nickName
      setUser(userInfo.value)
    }
  }

  /**
   * 修改密码
   */
  async function updatePassword(passwordForm) {
    await updatePasswordApi(passwordForm)
  }

  /**
   * 退出登录
   */
  function doLogout() {
    token.value = ''
    userInfo.value = null
    removeToken()
    removeUser()
  }

  return {
    token,
    userInfo,
    login,
    register,
    fetchUserInfo,
    updateNickname,
    updatePassword,
    doLogout
  }
})
