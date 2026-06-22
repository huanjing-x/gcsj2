<template>
  <div class="code-editor-wrapper">
    <div class="editor-toolbar">
      <div class="toolbar-left">
        <span class="language-label">{{ languageMap[language] || language }}</span>
      </div>
      <div class="toolbar-right">
        <el-switch
          v-model="isDark"
          active-text="深色"
          inactive-text="浅色"
          @change="toggleTheme"
          size="small"
        />
      </div>
    </div>
    <div ref="editorContainer" class="editor-container"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import * as monaco from 'monaco-editor'

// 配置 Monaco 使用 CDN 加载（生产环境建议本地部署）
self.MonacoEnvironment = {
  getWorker(_, label) {
    const getWorkerModule = (moduleUrl, label) => {
      return new Worker(self.MonacoEnvironment.getWorkerUrl(moduleUrl), {
        name: label,
        type: 'module'
      })
    }
    return getWorkerModule('/monaco-editor/esm/vs/editor/editor.worker.js', label)
  }
}

const props = defineProps({
  modelValue: { type: String, default: '' },
  language: { type: String, default: 'java' },
  theme: { type: String, default: 'vs' },
  readonly: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue'])

const editorContainer = ref(null)
const isDark = ref(props.theme === 'vs-dark')
let editor = null

// 语言映射表
const languageMap = {
  java: 'Java',
  python: 'Python',
  javascript: 'JavaScript',
  go: 'Go',
  sql: 'SQL',
  html: 'HTML',
  css: 'CSS',
  typescript: 'TypeScript',
  json: 'JSON',
  xml: 'XML',
  yaml: 'YAML',
  c: 'C',
  cpp: 'C++',
  csharp: 'C#',
  php: 'PHP',
  ruby: 'Ruby',
  rust: 'Rust',
  shell: 'Shell',
  bash: 'Bash'
}

onMounted(async () => {
  await nextTick()
  if (editorContainer.value) {
    editor = monaco.editor.create(editorContainer.value, {
      value: props.modelValue,
      language: props.language,
      theme: props.theme,
      readOnly: props.readonly,
      automaticLayout: true,
      minimap: { enabled: false },
      fontSize: 14,
      lineNumbers: 'on',
      scrollBeyondLastLine: false,
      wordWrap: 'on',
      tabSize: 4,
      autoIndent: 'full',
      formatOnPaste: true,
      suggest: { showKeywords: true, showSnippets: true }
    })

    // 内容变化同步到 v-model
    editor.onDidChangeModelContent(() => {
      const value = editor.getValue()
      emit('update:modelValue', value)
    })
  }
})

onBeforeUnmount(() => {
  if (editor) {
    editor.dispose()
    editor = null
  }
})

// 监听外部值变化
watch(() => props.modelValue, (newVal) => {
  if (editor && editor.getValue() !== newVal) {
    editor.setValue(newVal)
  }
})

// 监听语言变化
watch(() => props.language, (newLang) => {
  if (editor) {
    monaco.editor.setModelLanguage(editor.getModel(), newLang)
  }
})

// 切换主题
function toggleTheme(val) {
  const newTheme = val ? 'vs-dark' : 'vs'
  if (editor) {
    monaco.editor.setTheme(newTheme)
  }
}

// 暴露方法：设置编辑内容
function setValue(val) {
  if (editor) {
    editor.setValue(val)
  }
}

// 暴露方法：获取编辑内容
function getValue() {
  return editor ? editor.getValue() : ''
}

defineExpose({ setValue, getValue })
</script>

<style scoped lang="scss">
.code-editor-wrapper {
  border: 1px solid var(--border-color);
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;

  .editor-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 6px 12px;
    background: #f0f0f0;
    border-bottom: 1px solid var(--border-color);
    font-size: 13px;
    flex-shrink: 0;

    .language-label {
      font-weight: 500;
      color: var(--text-color);
    }
  }

  .editor-container {
    flex: 1;
    min-height: 300px;
  }
}
</style>
