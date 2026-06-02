import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '../views/layout/Layout.vue'

const routes = [
  // 访问根目录重定向到登录页
  {
    path: '/',
    redirect: '/login'
  },
  // 独立的登录页路由 (没有嵌套在 Layout 里，因为它不需要左侧菜单)
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue')
  },
  // 后台主界面
  {
    path: '/layout',
    name: 'Layout',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: '/home', // 注意这里，可以加 / 变成绝对路径
        name: 'Home',
        component: () => import('../views/HomeView.vue')
      },
      {
        path: '/company', // 必须要和左侧菜单 Sidebar.vue 里的 index 对应上！
        name: 'Company',
        component: () => import('../views/CompanyView.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router