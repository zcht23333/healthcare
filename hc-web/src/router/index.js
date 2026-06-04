import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/layout/Layout.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/LoginView.vue') },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('../views/HomeView.vue') },
      { path: 'company', name: 'Company', component: () => import('../views/CompanyView.vue') },
      { path: 'drug', name: 'Drug', component: () => import('../views/DrugView.vue') },
      { path: 'doctor', name: 'Doctor', component: () => import('../views/DoctorView.vue') },
      { path: 'policy', name: 'Policy', component: () => import('../views/PolicyView.vue') },
      { path: 'pharmaPolicy', name: 'PharmaPolicy', component: () => import('../views/PharmaPolicyView.vue') },
      { path: 'location', name: 'Location', component: () => import('../views/LocationView.vue') },
      { path: 'city', name: 'City', component: () => import('../views/CityView.vue') },
      { path: 'material', name: 'Material', component: () => import('../views/MaterialView.vue') }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫 (逻辑不变)
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else {
    if (!token) next('/login')
    else next()
  }
})

export default router