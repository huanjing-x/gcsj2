<template>
  <div class="tag-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>标签管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增标签
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="tagName" label="标签名称" min-width="250">
          <template #default="{ row }">
            <el-tag size="default" type="primary">{{ row.tagName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          background
          @current-change="loadData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑标签' : '新增标签'"
      width="400px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标签名称" prop="tagName">
          <el-input v-model="form.tagName" placeholder="请输入标签名称" maxlength="30" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { pageTagsApi, addTagApi, updateTagApi, deleteTagApi } from '@/api/tag'
import { useCodeStore } from '@/stores/code'
import { ElMessage, ElMessageBox } from 'element-plus'

const codeStore = useCodeStore()

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const formRef = ref(null)
const form = reactive({
  tagName: ''
})

const rules = {
  tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
}

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await pageTagsApi({ current: currentPage.value, size: pageSize.value })
    tableData.value = res.data.records
    total.value = res.data.total
    // 同步刷新下拉框数据
    codeStore.loadTags()
  } catch (e) {
    // 已在拦截器处理
  } finally {
    loading.value = false
  }
}

function handleAdd() {
  isEdit.value = false
  editId.value = null
  form.tagName = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editId.value = row.id
  form.tagName = row.tagName
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateTagApi({ id: editId.value, ...form })
      ElMessage.success('标签修改成功')
    } else {
      await addTagApi({ ...form })
      ElMessage.success('标签创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    // 已在拦截器处理
  } finally {
    submitting.value = false
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除标签「${row.tagName}」吗？删除后代码上的该标签也会被移除。`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTagApi(row.id)
      ElMessage.success('标签删除成功')
      loadData()
    } catch (e) {
      // 已在拦截器处理
    }
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.tag-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
  }

  .pagination-wrap {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
  }
}
</style>
