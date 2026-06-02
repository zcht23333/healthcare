const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // 添加下面这段配置
  devServer: {
    port: 8081, // 显式指定前端运行端口
    open: true  // 运行 npm run serve 时自动打开浏览器
  }
})