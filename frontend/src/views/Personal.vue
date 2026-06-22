<template>
  <div class="personal-page">
    <el-row :gutter="20">
      <!-- 修改昵称 -->
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <span>修改昵称</span>
          </template>
          <el-form ref="nickFormRef" :model="nickForm" label-width="80px">
            <el-form-item label="当前昵称">
              <el-input :model-value="userStore.userInfo?.nickName" disabled />
            </el-form-item>
            <el-form-item label="新昵称" prop="nickName">
              <el-input v-model="nickForm.nickName" placeholder="请输入新昵称" maxlength="32" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="nickLoading" @click="handleNickname">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 修改密码 -->
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <span>修改密码</span>
          </template>
          <el-form
            ref="pwdFormRef"
            :model="pwdForm"
            :rules="pwdRules"
            label-width="80px"
          >
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码（6-16位）" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="pwdLoading" @click="handlePassword">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 账号信息 -->
    <el-card shadow="never" style="margin-top: 20px;">
      <template #header>
        <span>账号信息</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">
          {{ userStore.userInfo?.userId || userStore.userInfo?.id }}
        </el-descriptions-item>
        <el-descriptions-item label="登录账号">
          {{ userStore.userInfo?.username }}
        </el-descriptions-item>
        <el-descriptions-item label="昵称">
          {{ userStore.userInfo?.nickName || userStore.userInfo?.nick_name }}
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">
          {{ userStore.userInfo?.createTime || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

// ============ 昵称 ============
const nickFormRef = ref(null)
const nickLoading = ref(false)
const nickForm = reactive({ nickName: '' })

async function handleNickname() {
  if (!nickForm.nickName.trim()) {
    ElMessage.warning('请输入新昵称')
    return
  }
  nickLoading.value = true
  try {
    await userStore.updateNickname(nickForm.nickName.trim())
    ElMessage.success('昵称修改成功')
    nickForm.nickName = ''
  } catch (e) {
    // 已在拦截器处理
  } finally {
    nickLoading.value = false
  }
}

// ============ 密码 ============
const pwdFormRef = ref(null)
const pwdLoading = ref(false)
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validatePwdConfirm = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码长度需在6-16位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validatePwdConfirm, trigger: 'blur' }
  ]
}

async function handlePassword() {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return

  pwdLoading.value = true
  try {
    await userStore.updatePassword({ ...pwdForm })
    ElMessage.success('密码修改成功，请重新登录')
    pwdFormRef.value.resetFields()
  } catch (e) {
    // 已在拦截器处理
  } finally {
    pwdLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.personal-page {
  max-width: 900px;
}
</style>
