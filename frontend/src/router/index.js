import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', noAuth: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册', noAuth: true }
  },
  {
    path: '/',
    redirect: '/home/code/list'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/components/AppLayout.vue'),
    redirect: '/home/code/list',
    children: [
      {
        path: 'code/list',
        name: 'CodeList',
        component: () => import('@/views/code/CodeList.vue'),
        meta: { title: '代码列表' }
      },
      {
        path: 'code/add',
        name: 'CodeAdd',
        component: () => import('@/views/code/CodeAdd.vue'),
        meta: { title: '新增代码' }
      },
      {
        path: 'code/edit/:id',
        name: 'CodeEdit',
        component: () => import('@/views/code/CodeEdit.vue'),
        meta: { title: '编辑代码' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/Category.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'tag',
        name: 'Tag',
        component: () => import('@/views/Tag.vue'),
        meta: { title: '标签管理' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: { title: '系统设置' }
      },
      {
        path: 'personal',
        name: 'Personal',
        component: () => import('@/views/Personal.vue'),
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 鉴权
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 代码片段管理` : '代码片段管理系统'

  if (to.meta.noAuth) {
    // 登录/注册页：已登录则跳转首页
    if (isLoggedIn() && (to.path === '/login' || to.path === '/register')) {
      next('/home/code/list')
    } else {
      next()
    }
  } else {
    // 需要登录的页面：未登录跳转登录页
    if (!isLoggedIn()) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
