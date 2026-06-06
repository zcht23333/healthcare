<template>
  <div class="home-container">
    <div class="welcome-header">
      <h2>👋 欢迎，超级管理员！今天又是顺利的一天。</h2>
      <p>今天是 {{ currentDate }}，系统各项服务运行健康，以下是实时数据概览。</p>
    </div>

    <!-- 1. 现代拟物化数据卡片区 -->
    <el-row :gutter="24" class="data-cards">
      <el-col :span="6">
        <div class="stat-card">
          <div class="icon-wrapper blue">
            <i class="el-icon-office-building"></i>
          </div>
          <div class="stat-info">
            <div class="stat-title">入驻医药公司</div>
            <div class="stat-value">{{ statData.totalCompany }} <span class="unit">家</span></div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="icon-wrapper green">
            <i class="el-icon-first-aid-kit"></i>
          </div>
          <div class="stat-info">
            <div class="stat-title">平台收录药品</div>
            <div class="stat-value">{{ statData.totalDrug }} <span class="unit">种</span></div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="icon-wrapper orange">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="stat-info">
            <div class="stat-title">注册医生总数</div>
            <div class="stat-value">{{ statData.totalDoctor }} <span class="unit">人</span></div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="icon-wrapper purple">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-info">
            <div class="stat-title">发布医保政策</div>
            <div class="stat-value">{{ statData.totalPolicy }} <span class="unit">项</span></div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 2. ECharts 莫兰迪双图表展示区 -->
    <el-row :gutter="24" style="margin-bottom: 24px;">
      <el-col :span="12">
        <div class="chart-card">
          <div class="card-header">各医药公司药品占比</div>
          <div ref="pieChartRef" style="height: 320px; width: 100%;"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="card-header">各科室医生人数分布</div>
          <div ref="barChartRef" style="height: 320px; width: 100%;"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 3. 全局快捷入口区 (定制悬浮块) -->
    <div class="chart-card">
      <div class="card-header">系统核心业务快捷通道</div>
      <div class="quick-actions-grid">
        <div class="quick-btn" @click="$router.push('/company')">医药公司管理</div>
        <div class="quick-btn" @click="$router.push('/drug')">药品信息管理</div>
        <div class="quick-btn" @click="$router.push('/doctor')">医生信息管理</div>
        <div class="quick-btn" @click="$router.push('/policy')">医保政策发布</div>
        <div class="quick-btn" @click="$router.push('/pharmaPolicy')">医药公司政策</div>
        <div class="quick-btn" @click="$router.push('/location')">销售地点管理</div>
        <div class="quick-btn" @click="$router.push('/city')">城市信息管理</div>
        <div class="quick-btn" @click="$router.push('/material')">必备材料审查</div>
      </div>
    </div>
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

// 专属莫兰迪配色盘，极其优雅
const morandiColors = ['#a1c4fd', '#ffecd2', '#a8edea', '#fed6e3', '#e0c3fc', '#8fd3f4']

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
  // 1. 初始化优雅版饼图
  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    pieChart.setOption({
      color: morandiColors, 
      tooltip: { 
        trigger: 'item', 
        formatter: '{b} : {c} 种 ({d}%)',
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        borderColor: '#e2e8f0',
        textStyle: { color: '#4a5568' }
      },
      legend: { top: 'bottom', icon: 'circle', textStyle: { color: '#718096' } },
      series: [{
        name: '药品数量', type: 'pie', radius: [30, 110], center: ['50%', '45%'],
        roseType: 'area', 
        itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 }, 
        data: statData.pieData
      }]
    })
  }

  // 2. 初始化优雅版柱状图
  if (barChartRef.value) {
    barChart = echarts.init(barChartRef.value)
    const deptNames = statData.barData.map(item => item.name || '未知科室')
    const deptValues = statData.barData.map(item => item.value)
    
    barChart.setOption({
      tooltip: { 
        trigger: 'axis', 
        axisPointer: { type: 'shadow' },
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        borderColor: '#e2e8f0',
        textStyle: { color: '#4a5568' }
      },
      grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
      xAxis: { 
        type: 'category', data: deptNames, 
        axisLine: { lineStyle: { color: '#cbd5e0' } },
        axisLabel: { color: '#718096', interval: 0, rotate: 30 }
      },
      yAxis: { 
        type: 'value',
        splitLine: { lineStyle: { color: '#f7fafc', type: 'dashed' } },
        axisLabel: { color: '#718096' }
      },
      series: [{
        name: '医生人数', type: 'bar', barWidth: '35%', data: deptValues,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#a1c4fd' }, 
            { offset: 1, color: '#c2e9fb' }
          ]),
          borderRadius: [6, 6, 0, 0]
        }
      }]
    })
  }

  window.addEventListener('resize', () => {
    if (pieChart) pieChart.resize()
    if (barChart) barChart.resize()
  })
}

onMounted(() => { fetchDashboardData() })
</script>

<style scoped>
.home-container {
  padding: 5px;
}

/* 欢迎语设计更温和 */
.welcome-header {
  margin-bottom: 28px;
}
.welcome-header h2 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 24px;
  font-weight: 600;
}
.welcome-header p {
  margin: 0;
  color: #a0aec0;
  font-size: 14px;
  letter-spacing: 0.5px;
}

/* 顶部四大数据卡片 - 纯白微阴影拟物风 */
.data-cards {
  margin-bottom: 24px;
}
.stat-card {
  background: #ffffff;
  border-radius: 20px;
  padding: 24px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(161, 196, 253, 0.15);
}
.icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 18px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 32px;
  margin-right: 20px;
}
/* 低饱和度底色配合对应高亮色图标 */
.blue { background: #eff6ff; color: #3b82f6; }
.green { background: #f0fdf4; color: #22c55e; }
.orange { background: #fff7ed; color: #f97316; }
.purple { background: #faf5ff; color: #a855f7; }

.stat-info {
  display: flex;
  flex-direction: column;
}
.stat-title {
  font-size: 14px;
  color: #718096;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #2d3748;
  line-height: 1;
}
.stat-value .unit {
  font-size: 14px;
  font-weight: normal;
  color: #a0aec0;
}

/* 图表与快捷操作的外层卡片 */
.chart-card {
  background: #ffffff;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
}
.card-header {
  font-weight: 600;
  color: #2d3748;
  font-size: 16px;
  margin-bottom: 20px;
  position: relative;
  padding-left: 12px;
}
/* 标题左侧的优雅细线 */
.card-header::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background: linear-gradient(180deg, #a1c4fd 0%, #c2e9fb 100%);
  border-radius: 2px;
}

/* 重构的快捷入口网格 (替换原有的 el-button) */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.quick-btn {
  background: #f8fafc;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4a5568;
  font-weight: 600;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
}
.quick-btn .emoji {
  margin-right: 8px;
  font-size: 18px;
}
.quick-btn:hover {
  background: #ffffff;
  border-color: rgba(161, 196, 253, 0.4);
  box-shadow: 0 8px 24px rgba(161, 196, 253, 0.15);
  color: #5a8dee;
  transform: translateY(-2px);
}
</style>