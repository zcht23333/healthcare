<template>
  <div class="modern-header">
    <div class="left-section">
      <!-- 页面欢迎语，替代生硬的面包屑 -->
      <h3 class="page-title">Healthcare 智慧医疗管理平台</h3>
    </div>
    
    <div class="right-section">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-profile">
          <el-avatar :size="36" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" class="avatar"/>
          <span class="username">超级管理员</span>
          <el-icon class="dropdown-icon"><el-icon-caret-bottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="custom-dropdown">
            <el-dropdown-item command="profile">
              <el-icon><el-icon-user /></el-icon> 个人中心
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><el-icon-setting /></el-icon> 系统设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout" style="color: #f56c6c;">
              <el-icon><el-icon-switch-button /></el-icon> 退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Message as ElMessage, MessageBox as ElMessageBox } from 'element-ui'

const router = useRouter()

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定退出',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      localStorage.removeItem('token') // 清除令牌
      ElMessage.success('已安全退出')
      router.push('/login')
    }).catch(() => {})
  } else {
    ElMessage.info('该功能正在开发中...')
  }
}
</script>

<style scoped>
.modern-header {
  height: 70px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px); /* 毛玻璃效果 */
  border-bottom: 1px solid rgba(226, 232, 240, 0.6);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
}

.page-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #2d3748;
  letter-spacing: 0.5px;
}

/* 右侧用户区交互 */
.user-profile {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 20px;
  transition: background 0.3s;
}
.user-profile:hover {
  background: #f7fafc;
}
.avatar {
  border: 2px solid #e2e8f0;
}
.username {
  margin: 0 10px;
  font-size: 14px;
  font-weight: 500;
  color: #4a5568;
}
.dropdown-icon {
  color: #a0aec0;
}

/* 下拉菜单圆角美化 */
:deep(.custom-dropdown) {
  border-radius: 12px;
  padding: 8px;
}
</style>