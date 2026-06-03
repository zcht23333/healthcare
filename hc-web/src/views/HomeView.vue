<template>
  <div class="home-container">
    <div class="welcome-header">
      <h2>👋 欢迎回来，超级管理员！系统各项指标运行平稳。</h2>
      <p>今天是 {{ currentDate }}，以下是医疗管理系统的实时全息数据概览。</p>
    </div>

    <el-row :gutter="20" class="data-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card blue-card">
          <div class="card-content">
            <div class="card-info"><div class="card-title">入驻医药公司</div><div class="card-value">{{ statData.totalCompany }} <span class="unit">家</span></div></div>
            <el-icon class="card-icon"><OfficeBuilding /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card green-card">
          <div class="card-content">
            <div class="card-info"><div class="card-title">平台收录药品</div><div class="card-value">{{ statData.totalDrug }} <span class="unit">种</span></div></div>
            <el-icon class="card-icon"><FirstAidKit /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card orange-card">
          <div class="card-content">
            <div class="card-info"><div class="card-title">注册医生总数</div><div class="card-value">{{ statData.totalDoctor }} <span class="unit">人</span></div></div>
            <el-icon class="card-icon"><UserFilled /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card purple-card">
          <div class="card-content">
            <div class="card-info"><div class="card-title">发布医保政策</div><div class="card-value">{{ statData.totalPolicy }} <span class="unit">项</span></div></div>
            <el-icon class="card-icon"><Document /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header><div class="card-header">📊 各医药公司药品占比 </div></template>
          <div ref="pieChartRef" style="height: 350px; width: 100%;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header><div class="card-header">📈 各科室医生人数分布 </div></template>
          <div ref="barChartRef" style="height: 350px; width: 100%;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="chart-card">
      <template #header><div class="card-header">⚡ 系统核心业务快捷通道</div></template>
      <div class="quick-actions-grid">
        <el-button type="primary" plain @click="$router.push('/company')">医药公司管理</el-button>
        <el-button type="success" plain @click="$router.push('/drug')">药品信息管理</el-button>
        <el-button type="warning" plain @click="$router.push('/doctor')">医生信息管理</el-button>
        <el-button type="danger"  plain @click="$router.push('/policy')">医保政策发布</el-button>
        <el-button type="info"    plain @click="$router.push('/pharmaPolicy')">医药公司政策</el-button>
        <el-button type="primary" plain @click="$router.push('/location')">销售地点管理</el-button>
        <el-button type="success" plain @click="$router.push('/city')">城市信息管理</el-button>
        <el-button type="warning" plain @click="$router.push('/material')">必备材料审查</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { getStatOverview } from '@/api/stat'
import * as echarts from 'echarts'

const currentDate = ref(new Date().toLocaleDateString())
const statData = reactive({
  totalCompany: 0, totalDrug: 0, totalDoctor: 0, totalPolicy: 0, pieData: [], barData: []
})

const pieChartRef = ref(null)
const barChartRef = ref(null)
let pieChart = null
let barChart = null

const fetchDashboardData = async () => {
  try {
    const res = await getStatOverview()
    Object.assign(statData, res)
    await nextTick()
    initCharts()
  } catch (error) {
    console.error('获取大盘数据失败', error)
  }
}

const initCharts = () => {
  // 1. 初始化饼图
  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    pieChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b} : {c} 种 ({d}%)' },
      legend: { top: 'bottom' },
      series: [{
        name: '药品数量', type: 'pie', radius: [30, 110], center: ['50%', '45%'],
        roseType: 'area', itemStyle: { borderRadius: 8 }, data: statData.pieData
      }]
    })
  }

  // 2. 初始化柱状图
  if (barChartRef.value) {
    barChart = echarts.init(barChartRef.value)
    const deptNames = statData.barData.map(item => item.name || '未知科室')
    const deptValues = statData.barData.map(item => item.value)
    
    barChart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
      xAxis: { 
        type: 'category', data: deptNames, 
        axisLabel: { interval: 0, rotate: 30 } // 倾斜字体防止重叠
      },
      yAxis: { type: 'value' },
      series: [{
        name: '医生人数', type: 'bar', barWidth: '40%', data: deptValues,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' }, { offset: 0.5, color: '#188df0' }, { offset: 1, color: '#188df0' }
          ]),
          borderRadius: [4, 4, 0, 0]
        }
      }]
    })
  }

  // 响应式图表缩放
  window.addEventListener('resize', () => {
    if (pieChart) pieChart.resize()
    if (barChart) barChart.resize()
  })
}

onMounted(() => { fetchDashboardData() })
</script>

<style scoped>
.home-container { padding: 10px; }
.welcome-header { margin-bottom: 25px; }
.welcome-header h2 { margin: 0 0 10px 0; color: #303133; }
.welcome-header p { margin: 0; color: #909399; }
.data-cards { margin-bottom: 20px; }
.stat-card { border-radius: 8px; border: none; color: white; }
.blue-card { background: linear-gradient(135deg, #409EFF, #66b1ff); }
.green-card { background: linear-gradient(135deg, #67C23A, #85ce61); }
.orange-card { background: linear-gradient(135deg, #E6A23C, #ebb563); }
.purple-card { background: linear-gradient(135deg, #9c27b0, #ba68c8); }

.card-content { display: flex; justify-content: space-between; align-items: center; padding: 5px; }
.card-title { font-size: 15px; opacity: 0.9; margin-bottom: 10px; }
.card-value { font-size: 32px; font-weight: bold; }
.card-value .unit { font-size: 14px; font-weight: normal; }
.card-icon { font-size: 50px; opacity: 0.3; }

.chart-card { border-radius: 8px; }
.card-header { font-weight: bold; color: #303133; }
.quick-actions-grid { 
  display: grid; 
  grid-template-columns: repeat(4, 1fr); 
  gap: 15px; 
  padding: 10px 0; 
}
.quick-actions-grid .el-button {
  margin-left: 0;
  height: 50px;
  font-size: 15px;
  font-weight: bold;
}
</style>