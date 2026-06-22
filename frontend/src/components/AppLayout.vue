<template>
  <el-container class="app-layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="app-sidebar">
      <div class="logo-container">
        <el-icon :size="24"><Document /></el-icon>
        <span v-show="!isCollapse" class="logo-text">代码片段管理</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/home/code/list">
          <el-icon><List /></el-icon>
          <span>代码列表</span>
        </el-menu-item>
        <el-menu-item index="/home/code/add">
          <el-icon><Plus /></el-icon>
          <span>新增代码</span>
        </el-menu-item>
        <el-menu-item index="/home/category">
          <el-icon><FolderOpened /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/home/tag">
          <el-icon><PriceTag /></el-icon>
          <span>标签管理</span>
        </el-menu-item>
        <el-menu-item index="/home/settings">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
        <el-menu-item index="/home/personal">
          <el-icon><User /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="app-header">
        <div class="header-left">
          <el-icon
            class="collapse-btn"
            @click="isCollapse = !isCollapse"
            :size="20"
          >
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/home/code/list' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="activeTitle">{{ activeTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <span class="user-name">{{ userStore.userInfo?.nickName || userStore.userInfo?.username }}</span>
          <el-button type="danger" size="small" plain @click="handleLogout">退出</el-button>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

const activeMenu = computed(() => {
  // 编辑页面高亮到列表
  if (route.path.startsWith('/home/code/edit')) {
    return '/home/code/list'
  }
  return route.path
})

const activeTitle = computed(() => route.meta?.title || '')

function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.doLogout()
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.app-layout {
  height: 100vh;
  overflow: hidden;
}

.app-sidebar {
  background-color: #304156;
  overflow-y: auto;
  overflow-x: hidden;
  transition: width 0.3s;

  .logo-container {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    height: 60px;
    color: #fff;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);

    .logo-text {
      font-size: 16px;
      font-weight: 600;
      white-space: nowrap;
    }
  }

  .el-menu {
    border-right: none;
  }
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid var(--border-color);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  padding: 0 20px;
  height: var(--header-height);

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .collapse-btn {
      cursor: pointer;
      &:hover { color: var(--primary-color); }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;

    .user-name {
      font-size: 14px;
      color: var(--text-secondary);
    }
  }
}

.app-main {
  background: var(--bg-color);
  padding: 20px;
  overflow-y: auto;
  height: calc(100vh - var(--header-height));
}

// 移动端适配
@media (max-width: 768px) {
  .app-sidebar {
    width: 64px !important;
    .logo-text { display: none; }
  }
  .app-main {
    padding: 12px;
  }
}
</style>
