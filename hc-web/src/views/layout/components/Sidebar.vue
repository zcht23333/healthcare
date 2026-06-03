<template>
  <el-aside width="210px" class="aside">
    <div class="logo">医药管理系统</div>
    <el-menu
      active-text-color="#409EFF"
      background-color="#304156"
      text-color="#fff"
      router
      :default-active="$route.path"
    >
      <el-menu-item index="/home">
        <el-icon><DataLine /></el-icon>
        <span>首页大盘</span>
      </el-menu-item>
      <el-menu-item index="/company">
        <el-icon><OfficeBuilding /></el-icon>
        <span>医药公司管理</span>
      </el-menu-item>
      <el-menu-item index="/drug">
        <el-icon><FirstAidKit /></el-icon>
        <span>药品信息管理</span>
      </el-menu-item>
      <el-menu-item index="/policy">
        <el-icon><Document /></el-icon>
        <span>医保政策管理</span>
      </el-menu-item>
      <el-menu-item index="/city">
        <el-icon><Location /></el-icon>
        <span>城市信息管理</span>
      </el-menu-item>
      <el-menu-item index="/location">
        <el-icon><Store /></el-icon>
        <span>销售地点管理</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<style scoped>
.aside {
  background-color: #304156;
  color: white;
  height: 100vh;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
  letter-spacing: 1px;
}
.el-menu {
  border-right: none;
}
</style>