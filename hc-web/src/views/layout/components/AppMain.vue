<template>
  <el-main class="main-content">
    <router-view />
  </el-main>
</template>

<style scoped>
.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  height: calc(100vh - 60px); /* 减去顶部 header 的高度 */
  overflow-y: auto;
}
</style>