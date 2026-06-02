<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <h2>医药政策管理系统</h2>
        <p>系统登录</p>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入管理员账号" prefix-icon="User" />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        
        <el-form-item prop="code">
          <div class="captcha-wrapper">
            <el-input 
              v-model="loginForm.code" 
              placeholder="请输入验证码" 
              prefix-icon="Key" 
              @keyup.enter="handleLogin"
              class="captcha-input" 
            />
            <img :src="captchaImg" @click="getCaptcha" class="captcha-img" title="点击刷新验证码" alt="验证码" />
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { fetchCaptcha, login } from '@/api/login'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

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
    // 把后端传来的 img 赋值给 img 标签
    captchaImg.value = data.img
    // 把后端传来的 uuid 存入表单，登录时要一起提交
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
      loading.value = true // 开启按钮加载动画
      try {
        // 1. 发起真实登录请求
        const token = await login(loginForm)
        
        // 2. 提示成功并保存 Token 到本地存储
        ElMessage.success('登录成功！欢迎回来。')
        localStorage.setItem('token', token)
        
        // 3. 路由跳转到后台主页
        router.push('/home')
        
      } catch (error) {
        // 登录失败（密码错或验证码错），拦截器会自动弹窗报错
        // 我们需要清空用户刚才输入的验证码，并自动刷新一张新图片
        loginForm.code = ''
        getCaptcha()
      } finally {
        loading.value = false // 关闭按钮加载动画
      }
    } else {
      return false
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #2d3a4b; /* 经典的后台深色背景 */
}

.login-card {
  width: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.login-header p {
  color: #909399;
  font-size: 14px;
  margin-top: 10px;
}

.captcha-wrapper {
  display: flex;
  width: 100%;
  justify-content: space-between;
}

.captcha-input {
  flex: 1;
  margin-right: 15px;
}

.captcha-img {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.login-btn {
  width: 100%;
  font-size: 16px;
  letter-spacing: 4px;
}
</style>