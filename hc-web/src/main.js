import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 引入 Vue 2 版的 Element UI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// 引入我们的莫兰迪高级主题
import './assets/theme.css'

Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')