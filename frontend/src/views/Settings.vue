<template>
  <div class="settings-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
        </div>
      </template>

      <el-form ref="formRef" :model="form" label-width="140px" style="max-width: 700px">
        <el-divider content-position="left">AI 接口配置</el-divider>
        <el-alert
          title="配置 AI 接口后才能使用代码注释、BUG 检测、重构优化等功能。不配置不影响其他功能正常使用。"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        />

        <el-form-item label="接口地址">
          <el-input v-model="form.ai_api_url" placeholder="例如：https://api.deepseek.com/v1/chat/completions" />
          <div class="form-tip">支持 OpenAI 兼容格式的 Chat Completions 接口</div>
        </el-form-item>

        <el-form-item label="API 密钥">
          <el-input v-model="form.ai_api_key" type="password" show-password placeholder="请输入 API 密钥" />
        </el-form-item>

        <el-form-item label="模型名称">
          <el-input v-model="form.ai_model" placeholder="例如：deepseek-v4-pro、gpt-3.5-turbo" />
        </el-form-item>

        <el-divider content-position="left">常用配置参考</el-divider>
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="DeepSeek">
            接口地址：https://api.deepseek.com/v1/chat/completions<br/>
            模型：deepseek-v4-pro 或 deepseek-v4-flash
          </el-descriptions-item>
          <el-descriptions-item label="OpenAI">
            接口地址：https://api.openai.com/v1/chat/completions<br/>
            模型：gpt-3.5-turbo 或 gpt-4
          </el-descriptions-item>
          <el-descriptions-item label="其他兼容接口">
            任何兼容 OpenAI Chat Completions 格式的接口均可使用
          </el-descriptions-item>
        </el-descriptions>

        <el-form-item style="margin-top: 24px">
          <el-button type="primary" :loading="saving" @click="handleSave">保存配置</el-button>
          <el-button @click="handleTest" :loading="testing" :disabled="!form.ai_api_url || !form.ai_api_key">
            测试连接
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getConfigApi, saveConfigApi } from '@/api/config'
import { aiProcessApi } from '@/api/ai'
import { ElMessage } from 'element-plus'

const formRef = ref(null)
const saving = ref(false)
const testing = ref(false)

const form = reactive({
  ai_api_url: '',
  ai_api_key: '',
  ai_model: ''
})

onMounted(async () => {
  try {
    const res = await getConfigApi()
    const config = res.data || {}
    form.ai_api_url = config.ai_api_url || ''
    form.ai_api_key = config.ai_api_key || ''
    form.ai_model = config.ai_model || ''
  } catch (e) {
    // 加载失败不影响使用
  }
})

async function handleSave() {
  saving.value = true
  try {
    await saveConfigApi({
      ai_api_url: form.ai_api_url,
      ai_api_key: form.ai_api_key,
      ai_model: form.ai_model
    })
    ElMessage.success('配置保存成功')
  } catch (e) {
    // 拦截器已处理
  } finally {
    saving.value = false
  }
}

async function handleTest() {
  testing.value = true
  try {
    // 发送一段简单代码测试 AI 是否连通
    await aiProcessApi({
      codeContent: 'print("hello world")',
      language: 'python',
      actionType: 'comment'
    })
    ElMessage.success('AI 接口连接正常')
  } catch (e) {
    ElMessage.error('连接失败，请检查配置是否正确')
  } finally {
    testing.value = false
  }
}
</script>

<style scoped lang="scss">
.settings-page {
  max-width: 800px;

  .card-header {
    font-size: 16px;
    font-weight: 600;
  }

  .form-tip {
    font-size: 12px;
    color: var(--text-secondary);
    margin-top: 4px;
  }
}
</style>
