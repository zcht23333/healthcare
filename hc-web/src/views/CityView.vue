<template>
  <div class="city-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="城市名称">
            <el-input v-model="queryParams.cityName" placeholder="请输入城市名称" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增城市</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="cityName" label="城市名称" min-width="150" align="center" />
        <el-table-column prop="province" label="所属省份" min-width="150" align="center" />
        <el-table-column prop="createTime" label="录入时间" width="200" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          :current-page.sync="queryParams.pageNum"
          :page-size.sync="queryParams.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="400px" @close="handleCloseDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="城市名称" prop="cityName">
          <el-input v-model="form.cityName" placeholder="如：深圳市" />
        </el-form-item>
        <el-form-item label="所属省份" prop="province">
          <el-input v-model="form.province" placeholder="如：广东省" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
// 【修复5】引入 nextTick
import { ref, reactive, onMounted, nextTick } from 'vue'
import { getCityList, addCity, updateCity, deleteCity } from '@/api/city'
import { Message as ElMessage, MessageBox as ElMessageBox } from 'element-ui'

const queryParams = reactive({ pageNum: 1, pageSize: 10, cityName: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)

onMounted(() => fetchList())
const fetchList = async () => {
  loading.value = true
  try {
    const res = await getCityList(queryParams)
    tableData.value = res.list; total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}
const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.cityName = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

const dialogVisible = ref(false)
const dialogTitle = ref('新增城市')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ id: null, cityName: '', province: '' })
const rules = reactive({
  cityName: [{ required: true, message: '城市名称不能为空', trigger: 'blur' }],
  province: [{ required: true, message: '所属省份不能为空', trigger: 'blur' }]
})

// 【修复6】使用 nextTick 并在弹窗挂载后彻底清空表单
const handleAdd = () => { 
  dialogTitle.value = '新增城市'
  dialogVisible.value = true
  nextTick(() => {
    if (formRef.value) formRef.value.resetFields()
    form.id = null
    Object.assign(form, { cityName: '', province: '' })
  })
}

// 【修复7】使用 nextTick 延迟赋值，避免破坏 Element UI 的表单初始状态
const handleEdit = (row) => { 
  dialogTitle.value = '修改城市'
  dialogVisible.value = true 
  nextTick(() => {
    Object.assign(form, row)
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`删除城市【${row.cityName}】?`, '警告', { type: 'warning' }).then(async () => {
    await deleteCity(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}
const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
  // 防御性判断
  if (!formRef.value) return
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) await updateCity(form)
        else await addCity(form)
        ElMessage.success(form.id ? '修改成功' : '新增成功')
        dialogVisible.value = false
        fetchList()
      } catch (error) { console.error(error) } finally { submitLoading.value = false }
    }
  })
}
</script>

<style scoped>
.header-action { display: flex; justify-content: space-between; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>