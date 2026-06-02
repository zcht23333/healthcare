<template>
  <el-header class="header">
    <div class="header-left">
      <el-icon class="fold-btn"><Fold /></el-icon>
      <span class="breadcrumb">欢迎来到医药销售与政策管理系统</span>
    </div>
    <div class="header-right">
      <el-dropdown>
        <span class="user-info">
          <el-avatar size="small" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
          <span class="username">管理员</span>
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<style scoped>
.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}
.header-left, .header-right {
  display: flex;
  align-items: center;
}
.fold-btn {
  font-size: 20px;
  cursor: pointer;
  margin-right: 15px;
}
.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  outline: none;
}
.username {
  margin-left: 8px;
  color: #606266;
}
</style>