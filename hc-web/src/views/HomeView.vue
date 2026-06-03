<template>
  <div class="home-container">
    <div class="welcome-header">
      <h2>👋 欢迎回来，超级管理员！</h2>
      <p>今天是 {{ currentDate }}，这是您系统的最新数据概览。</p>
    </div>

    <el-row :gutter="20" class="data-cards">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card blue-card">
          <div class="card-content">
            <div class="card-info">
              <div class="card-title">入驻医药公司</div>
              <div class="card-value">{{ statData.totalCompany }} <span class="unit">家</span></div>
            </div>
            <el-icon class="card-icon"><OfficeBuilding /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="stat-card green-card">
          <div class="card-content">
            <div class="card-info">
              <div class="card-title">平台收录药品</div>
              <div class="card-value">{{ statData.totalDrug }} <span class="unit">种</span></div>
            </div>
            <el-icon class="card-icon"><FirstAidKit /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="stat-card orange-card">
          <div class="card-content">
            <div class="card-info">
              <div class="card-title">系统安全运行</div>
              <div class="card-value">99.9 <span class="unit">%</span></div>
            </div>
            <el-icon class="card-icon"><Monitor /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>各公司药品数量占比分布</span>
            </div>
          </template>
          <div ref="pieChartRef" style="height: 350px; width: 100%;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
         <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>系统快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button type="primary" size="large" @click="$router.push('/company')">🏢 医药公司管理</el-button>
            <el-button type="success" size="large" @click="$router.push('/drug')">💊 药品信息管理</el-button>
            <el-button type="warning" size="large">📄 发布医保政策 (待开发)</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { getStatOverview } from '@/api/stat'
import * as echarts from 'echarts'

const currentDate = ref(new Date().toLocaleDateString())
const statData = reactive({
  totalCompany: 0,
  totalDrug: 0,
  pieData: []
})

// ECharts 容器 DOM 的引用
const pieChartRef = ref(null)

const fetchDashboardData = async () => {
  try {
    const res = await getStatOverview()
    statData.totalCompany = res.totalCompany
    statData.totalDrug = res.totalDrug
    statData.pieData = res.pieData

    // 数据拿到了，等 DOM 渲染完后初始化图表
    await nextTick()
    initChart()
  } catch (error) {
    console.error('获取大盘数据失败', error)
  }
}

const initChart = () => {
  if (!pieChartRef.value) return
  
  const myChart = echarts.init(pieChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b} : {c} 种 ({d}%)' // 悬浮提示格式
    },
    legend: {
      top: 'bottom'
    },
    series: [
      {
        name: '药品数量',
        type: 'pie',
        radius: [30, 110],
        center: ['50%', '45%'],
        roseType: 'area', // 南丁格尔玫瑰图模式
        itemStyle: {
          borderRadius: 8
        },
        data: statData.pieData // 绑定后端传来的动态数据
      }
    ]
  }
  myChart.setOption(option)

  // 监听窗口大小变化，图表自适应缩放
  window.addEventListener('resize', () => {
    myChart.resize()
  })
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.home-container {
  padding: 10px;
}
.welcome-header {
  margin-bottom: 30px;
}
.welcome-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}
.welcome-header p {
  margin: 0;
  color: #909399;
}
.data-cards {
  margin-bottom: 20px;
}
.stat-card {
  border-radius: 8px;
  border: none;
  color: white;
}
.blue-card { background: linear-gradient(135deg, #409EFF, #66b1ff); }
.green-card { background: linear-gradient(135deg, #67C23A, #85ce61); }
.orange-card { background: linear-gradient(135deg, #E6A23C, #ebb563); }

.card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}
.card-title {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 10px;
}
.card-value {
  font-size: 36px;
  font-weight: bold;
}
.card-value .unit {
  font-size: 16px;
  font-weight: normal;
}
.card-icon {
  font-size: 60px;
  opacity: 0.3;
}

.chart-card {
  border-radius: 8px;
}
.card-header {
  font-weight: bold;
  color: #303133;
}
.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
}
</style>