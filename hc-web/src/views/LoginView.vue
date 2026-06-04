<template>
  <div class="login-container">
    <div class="bg-shape shape-1"></div>
    <div class="bg-shape shape-2"></div>

    <div class="login-box">
      <div class="login-header">
        <div class="logo-icon">🌿</div>
        <h2>Healthcare</h2>
        <p>现代医药政策管理系统</p>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入管理员账号" 
            prefix-icon="User" 
            class="custom-input"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码" 
            prefix-icon="Lock" 
            show-password 
            class="custom-input"
          />
        </el-form-item>
        
        <el-form-item prop="code">
          <div class="captcha-wrapper">
            <el-input 
              v-model="loginForm.code" 
              placeholder="验证码" 
              prefix-icon="Key" 
              @keyup.enter="handleLogin"
              class="custom-input captcha-input" 
            />
            <div class="img-box" title="点击刷新验证码">
              <img :src="captchaImg" @click="getCaptcha" alt="验证码" v-if="captchaImg"/>
              <div v-else class="img-placeholder" @click="getCaptcha">加载中...</div>
            </div>
          </div>
        </el-form-item>

        <el-form-item style="margin-top: 30px;">
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>Secure & Smart Management</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { fetchCaptcha, login } from '@/api/login'
import router from '@/router' // 【关键修改1】在 Vue 2.7 中直接引入路由实例即可
import { Message as ElMessage } from 'element-ui' // 【关键修改2】替换为 Element UI

// 表单数据
const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  uuid: '' // 隐藏字段，用来发给后端寻找 Redis 里的验证码
})

// 表单校验规则
const rules = reactive({
  username: [{ required: true, message: '账号不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
  code: [{ required: true, message: '验证码不能为空', trigger: 'blur' }]
})

const loginFormRef = ref(null)
const loading = ref(false)
const captchaImg = ref('') // 存储图片 Base64

// 获取验证码的方法
const getCaptcha = async () => {
  try {
    const data = await fetchCaptcha()
    captchaImg.value = data.img
    loginForm.uuid = data.uuid
  } catch (error) {
    console.error('获取验证码失败', error)
  }
}

// 页面加载时自动获取一次验证码
onMounted(() => {
  getCaptcha()
})

// 点击登录按钮
const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const token = await login(loginForm)
        ElMessage.success('登录成功！欢迎回来。')
        localStorage.setItem('token', token)
        router.push('/home')
      } catch (error) {
        loginForm.code = ''
        getCaptcha()
      } finally {
        loading.value = false
      }
    } else {
      return false
    }
  })
}
</script>

<style scoped>
/* 整个背景：采用非常温柔的浅色渐变，带来轻松感 */
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%);
  position: relative;
  overflow: hidden;
}

/* 装饰性背景光晕，增加画面的空间感和优雅感 */
.bg-shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  z-index: 0;
}
.shape-1 {
  width: 500px;
  height: 500px;
  background: rgba(161, 196, 253, 0.4);
  top: -100px;
  left: -100px;
}
.shape-2 {
  width: 400px;
  height: 400px;
  background: rgba(194, 233, 251, 0.4);
  bottom: -50px;
  right: -50px;
}

/* 登录卡片主体：摒弃默认 el-card，使用自定义的毛玻璃拟物化设计 */
.login-box {
  width: 420px;
  padding: 50px 40px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.04), 
              inset 0 1px 0 rgba(255, 255, 255, 0.6);
  z-index: 1;
  transition: transform 0.3s ease;
}
.login-box:hover {
  transform: translateY(-2px);
}

/* 头部设计：留白增加，字体更优雅纤细 */
.login-header {
  text-align: center;
  margin-bottom: 40px;
}
.logo-icon {
  font-size: 42px;
  margin-bottom: 10px;
  line-height: 1;
}
.login-header h2 {
  margin: 0 0 8px;
  color: #2c3e50;
  font-size: 26px;
  font-weight: 600;
  letter-spacing: 1px;
}
.login-header p {
  color: #95a5a6;
  font-size: 14px;
  margin: 0;
  letter-spacing: 2px;
}

/* 表单区域间距优化 */
.login-form .el-form-item {
  margin-bottom: 24px;
}

/* 深度定制 Element Plus 输入框：去除生硬边框，改为现代微填充风格 */
:deep(.custom-input .el-input__wrapper) {
  background-color: #f8f9fa;
  box-shadow: none !important;
  border: 1px solid transparent;
  border-radius: 12px;
  padding: 4px 15px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
:deep(.custom-input .el-input__wrapper:hover) {
  background-color: #f1f3f5;
}
:deep(.custom-input .el-input__wrapper.is-focus) {
  background-color: #ffffff;
  border-color: #a1c4fd;
  box-shadow: 0 0 0 4px rgba(161, 196, 253, 0.15) !important;
}

/* 验证码同行排版布局优化 */
.captcha-wrapper {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 15px;
}
.captcha-input {
  flex: 1;
}
.img-box {
  width: 130px;
  height: 44px;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  background: #f8f9fa;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity 0.2s;
}
.img-box:hover {
  opacity: 0.8;
}
.img-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.img-placeholder {
  color: #adb5bd;
  font-size: 13px;
}

/* 极致丝滑的按钮设计 */
.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 6px;
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
  border: none;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
  box-shadow: 0 6px 15px rgba(161, 196, 253, 0.3);
  transition: all 0.3s ease;
}
.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(161, 196, 253, 0.4);
  background: linear-gradient(135deg, #8cb4f9 0%, #b3e2fa 100%);
}
.login-btn:active {
  transform: translateY(1px);
  box-shadow: 0 2px 8px rgba(161, 196, 253, 0.3);
}

/* 底部英文字体，增加品牌感 */
.login-footer {
  text-align: center;
  margin-top: 30px;
}
.login-footer p {
  color: #ced4da;
  font-size: 12px;
  font-weight: 500;
  letter-spacing: 1px;
}
</style>