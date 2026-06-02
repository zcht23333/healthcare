<template>
  <div class="home">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>前后端联调测试台</span>
          <el-button type="primary" @click="handleTestConnection">发起向后端的请求</el-button>
        </div>
      </template>
      <div class="result-box">
        <p>来自后端的问候：</p>
        <h2 v-if="backendMsg" class="success-text">{{ backendMsg }}</h2>
        <span v-else class="empty-text">暂无数据，请点击右上角按钮测试</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { fetchHello } from '@/api/test'
import { ElMessage } from 'element-plus'

// 响应式变量，存储后端返回的数据
const backendMsg = ref('')

// 按钮点击事件
const handleTestConnection = async () => {
  try {
    // 调用我们在 api/test.js 中定义的接口
    const data = await fetchHello()
    backendMsg.value = data // 直接赋值，拦截器已经帮我们把一层层的 JSON 剥干净了
    ElMessage.success('恭喜！前后端联调成功！')
  } catch (error) {
    console.error('联调失败:', error)
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.result-box {
  min-height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #f8f9fa;
  border-radius: 4px;
}
.success-text {
  color: #67C23A;
  margin-top: 10px;
}
.empty-text {
  color: #909399;
}
</style>