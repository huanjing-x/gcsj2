<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="80%"
    top="5vh"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <div class="dialog-code-info">
      <div class="info-row">
        <el-tag type="primary">{{ language }}</el-tag>
        <el-tag v-if="categoryName" type="success">{{ categoryName }}</el-tag>
        <el-tag v-for="tag in tags" :key="tag.id" type="info">{{ tag.tagName }}</el-tag>
      </div>
      <p v-if="remark" class="remark-text">{{ remark }}</p>
    </div>
    <CodeEditor
      v-if="visible"
      :model-value="codeContent"
      :language="language"
      :readonly="true"
      theme="vs-dark"
      style="height: 50vh;"
    />
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
      <el-button type="primary" @click="handleCopy">复制代码</el-button>
      <el-button type="success" @click="handleExport">导出文件</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import CodeEditor from './CodeEditor.vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  title: { type: String, default: '' },
  language: { type: String, default: 'java' },
  codeContent: { type: String, default: '' },
  remark: { type: String, default: '' },
  tags: { type: Array, default: () => [] },
  categoryName: { type: String, default: '' }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(false)

watch(() => props.modelValue, (val) => { visible.value = val })
watch(visible, (val) => { emit('update:modelValue', val) })

// 语言到文件扩展名映射
const extMap = {
  java: 'java', python: 'py', javascript: 'js', go: 'go',
  sql: 'sql', html: 'html', css: 'css', typescript: 'ts',
  json: 'json', xml: 'xml', yaml: 'yml', c: 'c', cpp: 'cpp',
  csharp: 'cs', php: 'php', ruby: 'rb', rust: 'rs', shell: 'sh'
}

function handleCopy() {
  navigator.clipboard.writeText(props.codeContent).then(() => {
    ElMessage.success('已复制到剪贴板')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

function handleExport() {
  const ext = extMap[props.language] || 'txt'
  const blob = new Blob([props.codeContent], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${props.title || 'code'}.${ext}`
  a.click()
  URL.revokeObjectURL(url)
  ElMessage.success('文件已导出')
}
</script>

<style scoped lang="scss">
.dialog-code-info {
  margin-bottom: 16px;

  .info-row {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 8px;
  }

  .remark-text {
    color: var(--text-secondary);
    font-size: 13px;
    line-height: 1.6;
    padding: 8px;
    background: #f5f7fa;
    border-radius: 4px;
  }
}
</style>
