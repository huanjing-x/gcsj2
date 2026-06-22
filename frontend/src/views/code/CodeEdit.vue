<template>
  <div class="code-edit-page" v-loading="pageLoading">
    <!-- 顶部表单栏 -->
    <div class="form-toolbar">
      <el-form :model="form" :rules="rules" ref="formRef" inline>
        <el-form-item prop="title">
          <el-input v-model="form.title" placeholder="代码标题" maxlength="100" style="width: 180px" />
        </el-form-item>
        <el-form-item prop="language">
          <el-select v-model="form.language" placeholder="编程语言" style="width: 130px">
            <el-option label="Java" value="java" />
            <el-option label="Python" value="python" />
            <el-option label="JavaScript" value="javascript" />
            <el-option label="Go" value="go" />
            <el-option label="SQL" value="sql" />
            <el-option label="TypeScript" value="typescript" />
            <el-option label="HTML" value="html" />
            <el-option label="CSS" value="css" />
            <el-option label="C" value="c" />
            <el-option label="C++" value="cpp" />
            <el-option label="C#" value="csharp" />
            <el-option label="PHP" value="php" />
            <el-option label="Ruby" value="ruby" />
            <el-option label="Rust" value="rust" />
            <el-option label="Shell" value="shell" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="form.categoryId" placeholder="分类（可选）" clearable style="width: 140px">
            <el-option v-for="cat in codeStore.categories" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="form.tagIds" placeholder="标签（可选）" multiple clearable collapse-tags style="width: 180px">
            <el-option v-for="tag in codeStore.tags" :key="tag.id" :label="tag.tagName" :value="tag.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">保存修改</el-button>
          <el-button @click="router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 代码编辑器 -->
    <div class="editor-area">
      <CodeEditor
        v-model="form.codeContent"
        :language="form.language || 'java'"
        :theme="editorTheme"
      />
    </div>

    <!-- 底部操作栏 -->
    <div class="form-footer">
      <el-input v-model="form.remark" placeholder="备注说明（可选）" maxlength="500" style="flex: 1" />
      <span style="color: var(--text-secondary); font-size: 13px; margin: 0 8px; white-space: nowrap">AI 辅助：</span>
      <el-button type="success" plain size="small" :disabled="!form.codeContent" :loading="aiLoading" @click="handleAi('comment')">
        添加注释
      </el-button>
      <el-button type="warning" plain size="small" :disabled="!form.codeContent" :loading="aiLoading" @click="handleAi('debug')">
        BUG 检测
      </el-button>
      <el-button type="danger" plain size="small" :disabled="!form.codeContent" :loading="aiLoading" @click="handleAi('refactor')">
        重构优化
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCodeStore } from '@/stores/code'
import { getSnippetDetailApi, updateSnippetApi } from '@/api/snippet'
import { aiProcessApi } from '@/api/ai'
import { ElMessage, ElMessageBox } from 'element-plus'
import CodeEditor from '@/components/CodeEditor.vue'

const route = useRoute()
const router = useRouter()
const codeStore = useCodeStore()

const formRef = ref(null)
const pageLoading = ref(false)
const submitting = ref(false)
const aiLoading = ref(false)
const editorTheme = ref('vs')
const snippetId = ref(null)

const form = reactive({
  title: '',
  language: 'java',
  categoryId: null,
  tagIds: [],
  codeContent: '',
  remark: ''
})

const rules = {
  title: [{ required: true, message: '请输入代码标题', trigger: 'blur' }],
  language: [{ required: true, message: '请选择语言', trigger: 'change' }],
}

onMounted(async () => {
  snippetId.value = route.params.id
  await Promise.all([codeStore.loadCategories(), codeStore.loadTags()])
  await loadDetail()
})

async function loadDetail() {
  pageLoading.value = true
  try {
    const res = await getSnippetDetailApi(snippetId.value)
    const data = res.data
    form.title = data.title
    form.language = data.language
    form.categoryId = data.categoryId
    form.codeContent = data.codeContent
    form.remark = data.remark || ''
    form.tagIds = data.tags ? data.tags.map(t => t.id) : []
  } catch (e) {
    // 已在拦截器处理
  } finally {
    pageLoading.value = false
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (!form.codeContent) {
    ElMessage.warning('请输入代码内容')
    return
  }
  submitting.value = true
  try {
    await updateSnippetApi({ id: Number(snippetId.value), ...form })
    ElMessage.success('代码片段更新成功')
    router.push('/home/code/list')
  } catch (e) {
    // 已在拦截器处理
  } finally {
    submitting.value = false
  }
}

async function handleAi(actionType) {
  if (!form.codeContent) {
    ElMessage.warning('请先输入代码内容')
    return
  }
  aiLoading.value = true
  try {
    const res = await aiProcessApi({
      codeContent: form.codeContent,
      language: form.language,
      actionType
    })
    const actionNames = { comment: '添加注释', debug: 'BUG检测', refactor: '重构优化' }
    ElMessageBox.confirm(
      `AI ${actionNames[actionType]}已完成，是否用结果覆盖编辑器内容？`,
      'AI 辅助',
      {
        confirmButtonText: '覆盖',
        cancelButtonText: '关闭',
        type: 'success',
        distinguishCancelAndClose: true,
        cancelButtonClass: 'el-button--info'
      }
    ).then(() => {
      form.codeContent = res.data
      ElMessage.success('已覆盖')
    }).catch(() => {})
  } catch (e) {
    // 已在拦截器处理
  } finally {
    aiLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.code-edit-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;

  .form-toolbar {
    background: #fff;
    padding: 10px 16px;
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    flex-shrink: 0;

    :deep(.el-form) {
      margin: 0;
      .el-form-item { margin-bottom: 0; margin-right: 12px; }
    }
  }

  .editor-area {
    flex: 1;
    min-height: 0;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    overflow: hidden;
  }

  .form-footer {
    display: flex;
    align-items: center;
    background: #fff;
    padding: 8px 16px;
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    flex-shrink: 0;
    gap: 6px;
  }
}
</style>
