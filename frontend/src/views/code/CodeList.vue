<template>
  <div class="code-list-page">
    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline>
        <el-form-item label="标题">
          <el-input
            v-model="queryForm.title"
            placeholder="模糊搜索"
            clearable
            style="width: 160px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="语言">
          <el-select v-model="queryForm.language" placeholder="全部" clearable style="width: 130px">
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
        <el-form-item label="分类">
          <el-select v-model="queryForm.categoryId" placeholder="全部分类" clearable style="width: 150px">
            <el-option
              v-for="cat in codeStore.categories"
              :key="cat.id"
              :label="cat.categoryName"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="queryForm.tagId" placeholder="全部标签" clearable style="width: 150px">
            <el-option
              v-for="tag in codeStore.tags"
              :key="tag.id"
              :label="tag.tagName"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-checkbox v-model="queryForm.isCollect">仅看收藏</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="title" label="标题" min-width="180">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="language" label="语言" width="100">
          <template #default="{ row }">
            <el-tag size="small" type="primary">{{ row.language }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120">
          <template #default="{ row }">
            <span v-if="row.categoryName">{{ row.categoryName }}</span>
            <span v-else style="color: #c0c4cc">未分类</span>
          </template>
        </el-table-column>
        <el-table-column prop="tags" label="标签" min-width="150">
          <template #default="{ row }">
            <el-tag
              v-for="tag in row.tags"
              :key="tag.id"
              size="small"
              type="info"
              style="margin-right: 4px;"
            >
              {{ tag.tagName }}
            </el-tag>
            <span v-if="!row.tags || row.tags.length === 0" style="color: #c0c4cc">无标签</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column prop="isCollect" label="收藏" width="70" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.isCollect === 1" style="color: #f7ba2a"><StarFilled /></el-icon>
            <el-icon v-else style="color: #c0c4cc"><Star /></el-icon>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" size="small" @click="handleCopy(row)">复制</el-button>
            <el-button link type="warning" size="small" @click="handleExport(row)">导出</el-button>
            <el-button link type="warning" size="small" @click="handleCollect(row)">
              {{ row.isCollect === 1 ? '取消收藏' : '收藏' }}
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="queryForm.current"
          v-model:page-size="queryForm.size"
          :total="total"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          background
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </el-card>

    <!-- 代码查看弹窗 -->
    <CodeDialog v-model="dialogVisible" v-bind="currentSnippet" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCodeStore } from '@/stores/code'
import { pageSnippetsApi, deleteSnippetApi, toggleCollectApi, getSnippetContentApi } from '@/api/snippet'
import { ElMessage, ElMessageBox } from 'element-plus'
import CodeDialog from '@/components/CodeDialog.vue'

const router = useRouter()
const codeStore = useCodeStore()

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const dialogVisible = ref(false)
const currentSnippet = ref({})

const queryForm = reactive({
  title: '',
  language: '',
  categoryId: null,
  tagId: null,
  isCollect: false,
  current: 1,
  size: 10
})

// 语言-扩展名映射
const extMap = {
  java: 'java', python: 'py', javascript: 'js', go: 'go',
  sql: 'sql', html: 'html', css: 'css', typescript: 'ts',
  json: 'json', xml: 'xml', yaml: 'yml', c: 'c', cpp: 'cpp',
  csharp: 'cs', php: 'php', ruby: 'rb', rust: 'rs', shell: 'sh'
}

onMounted(async () => {
  await Promise.all([codeStore.loadCategories(), codeStore.loadTags()])
  await loadData()
})

async function loadData() {
  loading.value = true
  try {
    const params = {
      title: queryForm.title || undefined,
      language: queryForm.language || undefined,
      categoryId: queryForm.categoryId || undefined,
      tagId: queryForm.tagId || undefined,
      isCollect: queryForm.isCollect || undefined,
      current: queryForm.current,
      size: queryForm.size
    }
    Object.keys(params).forEach(k => params[k] === undefined && delete params[k])
    const res = await pageSnippetsApi(params)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    // 已在拦截器处理
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryForm.current = 1
  loadData()
}

function handleReset() {
  queryForm.title = ''
  queryForm.language = ''
  queryForm.categoryId = null
  queryForm.tagId = null
  queryForm.isCollect = false
  handleSearch()
}

function handleView(row) {
  currentSnippet.value = {
    title: row.title,
    language: row.language,
    codeContent: row.codeContent,
    remark: row.remark,
    tags: row.tags || [],
    categoryName: row.categoryName || ''
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  router.push(`/home/code/edit/${row.id}`)
}

async function handleCopy(row) {
  try {
    const res = await getSnippetContentApi(row.id)
    await navigator.clipboard.writeText(res.data)
    ElMessage.success('已复制到剪贴板')
  } catch {
    ElMessage.error('复制失败')
  }
}

function handleExport(row) {
  const ext = extMap[row.language] || 'txt'
  const blob = new Blob([row.codeContent], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${row.title || 'code'}.${ext}`
  a.click()
  URL.revokeObjectURL(url)
  ElMessage.success('文件已导出')
}

async function handleCollect(row) {
  try {
    await toggleCollectApi(row.id)
    row.isCollect = row.isCollect === 1 ? 0 : 1
    ElMessage.success(row.isCollect === 1 ? '已收藏' : '已取消收藏')
  } catch (e) {
    // 已在拦截器处理
  }
}

function handleDelete(row) {
  ElMessageBox.confirm('删除后不可恢复，确定要删除该代码片段吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSnippetApi(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (e) {
      // 已在拦截器处理
    }
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.code-list-page {
  .search-card {
    margin-bottom: 16px;
    :deep(.el-form) { margin-bottom: 0; }
  }

  .table-card {
    .pagination-wrap {
      display: flex;
      justify-content: flex-end;
      margin-top: 16px;
    }
  }
}
</style>
