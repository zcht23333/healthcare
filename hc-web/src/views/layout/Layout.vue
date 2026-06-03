<template>
  <div class="app-wrapper">
    <el-container class="main-container">
      <Sidebar />
      <el-container class="content-container">
        <el-header style="padding: 0; height: auto;">
          <Header />
        </el-header>
        <!-- AppMain 装载各种视图 (表格、图表) -->
        <el-main class="app-main-box">
          <AppMain />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'
import AppMain from './components/AppMain.vue'
</script>

<style scoped>
.app-wrapper {
  height: 100vh;
  width: 100vw;
  overflow: hidden; /* 彻底切断 Layout 级别的外层溢出 */
}
.main-container {
  height: 100%;
}
.content-container {
  height: 100%;
  display: flex;
  flex-direction: column; /* 垂直排布：头部 + 核心区 */
}

/* 这里是灵魂所在：给内容区注入登录页同款的极浅呼吸背景 */
.app-main-box {
  flex: 1; /* 自动撑满 Header 以外的所有剩余空间 */
  background: linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%);
  padding: 24px;
  overflow-y: auto; /* 整个系统唯一合法且可见的纵向滚动条 */
  overflow-x: hidden;
  box-sizing: border-box;
}
</style>