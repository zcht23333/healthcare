<template>
  <el-container class="layout-container">
    <Sidebar />
    
    <el-container direction="vertical">
      <Header />
      <AppMain />
    </el-container>
  </el-container>
</template>

<script setup>
// Vue 3 <script setup> 语法，直接导入即可在模板中使用
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'
import AppMain from './components/AppMain.vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100%;
}
</style>