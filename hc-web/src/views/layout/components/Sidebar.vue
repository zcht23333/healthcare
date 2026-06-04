<template>
  <el-aside width="240px" class="modern-sidebar">
    <div class="logo-box">
      <span class="logo-icon">🌿</span>
      <span class="logo-text">Healthcare</span>
    </div>
    
    <!-- 全部交由底部的定制 CSS 控制 -->
    <el-menu
      class="modern-menu"
      router
      :default-active="$route.path"
    >
      <div class="menu-group-title">数据监控</div>
      <el-menu-item index="/home">
        <el-icon><el-icon-data-line /></el-icon>
        <span>首页大盘</span>
      </el-menu-item>

      <div class="menu-group-title">核心业务</div>
      <el-menu-item index="/company">
        <el-icon><el-icon-office-building /></el-icon>
        <span>医药公司管理</span>
      </el-menu-item>
      <el-menu-item index="/drug">
        <el-icon><el-icon-first-aid-kit /></el-icon>
        <span>药品信息管理</span>
      </el-menu-item>
      <el-menu-item index="/doctor">
        <el-icon><el-icon-user-solid /></el-icon>
        <span>医生信息管理</span>
      </el-menu-item>

      <div class="menu-group-title">政策与文档</div>
      <el-menu-item index="/policy">
        <el-icon><el-icon-document /></el-icon>
        <span>医保政策管理</span>
      </el-menu-item>
      <el-menu-item index="/pharmaPolicy">
        <el-icon><el-icon-postcard /></el-icon>
        <span>医药公司政策管理</span>
      </el-menu-item>
      <el-menu-item index="/material">
        <el-icon><el-icon-folder-opened /></el-icon>
        <span>必备材料管理</span>
      </el-menu-item>

      <div class="menu-group-title">网点配置</div>
      <el-menu-item index="/location">
        <el-icon><el-icon-s-shop /></el-icon>
        <span>销售地点管理</span>
      </el-menu-item>
      <el-menu-item index="/city">
        <el-icon><el-icon-location /></el-icon>
        <span>城市信息管理</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<style scoped>
/* 侧边栏整体：纯净白底色 + 微弱阴影分隔 */
.modern-sidebar {
  background-color: #ffffff;
  height: 100vh;
  box-shadow: 2px 0 20px rgba(0, 0, 0, 0.02);
  z-index: 10;
  display: flex;
  flex-direction: column;
}

/* Logo 区域：呼应登录页的设计 */
.logo-box {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  margin-bottom: 10px;
}
.logo-icon {
  font-size: 28px;
  margin-right: 10px;
}
.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  letter-spacing: 0.5px;
}

/* 菜单分组标题：增加层级感 */
.menu-group-title {
  padding: 15px 24px 5px;
  font-size: 12px;
  color: #a0aec0;
  letter-spacing: 1px;
  text-transform: uppercase;
  font-weight: 600;
}

/* 深度定制 Element Plus 菜单样式 */
:deep(.modern-menu) {
  border-right: none; /* 去除自带的右边框 */
  background: transparent;
  padding: 0 10px; /* 两侧留白，为胶囊形状做准备 */
}

/* 常规菜单项样式 */
:deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  margin-bottom: 6px;
  border-radius: 12px; /* 胶囊圆角 */
  color: #4a5568;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 鼠标悬停时的样式 */
:deep(.el-menu-item:hover) {
  background-color: rgba(161, 196, 253, 0.1) !important;
  color: #5a8dee !important;
}

/* 菜单激活时的终极视觉：渐变胶囊 + 柔和发光 */
:deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%) !important;
  color: #ffffff !important;
  box-shadow: 0 4px 12px rgba(161, 196, 253, 0.4);
  font-weight: 600;
}
:deep(.el-menu-item.is-active .el-icon) {
  color: #ffffff !important;
}
</style>