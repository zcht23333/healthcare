<template>
  <div class="location-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="地点名称">
            <el-input v-model="queryParams.locationName" placeholder="请输入地点名称" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增销售地点</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="locationName" label="销售地点名称" min-width="180" />
        <el-table-column prop="cityName" label="所在城市" min-width="120" align="center">
          <template #default="scope">
            <el-tag>{{ scope.row.cityName || '未知' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="120" align="center" />
        <el-table-column prop="contactPhone" label="联系电话" width="150" align="center" />
        <el-table-column prop="address" label="详细地址" min-width="200" show-overflow-tooltip />
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="handleCloseDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="地点名称" prop="locationName">
          <el-input v-model="form.locationName" placeholder="请输入药店或医院名称" />
        </el-form-item>
        <el-form-item label="所在城市" prop="cityId">
          <el-select v-model="form.cityId" placeholder="请选择所在城市" style="width: 100%">
            <el-option
              v-for="item in cityOptions"
              :key="item.id"
              :label="item.cityName + ' (' + item.province + ')'"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入详细街道门牌号" />
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
// 【修复8】引入 nextTick
import { ref, reactive, onMounted, nextTick } from 'vue'
import { getLocationList, addLocation, updateLocation, deleteLocation } from '@/api/location'
import { getCityList } from '@/api/city'
import { Message as ElMessage, MessageBox as ElMessageBox } from 'element-ui'

const queryParams = reactive({ pageNum: 1, pageSize: 10, locationName: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const cityOptions = ref([])

onMounted(() => {
  fetchList()
  fetchCityOptions()
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getLocationList(queryParams)
    tableData.value = res.list; total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}

const fetchCityOptions = async () => {
  try {
    const res = await getCityList({ pageNum: 1, pageSize: 1000 })
    cityOptions.value = res.list
  } catch (error) { console.error(error) }
}

const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.locationName = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

const dialogVisible = ref(false)
const dialogTitle = ref('新增销售地点')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ id: null, locationName: '', cityId: null, address: '', contactPerson: '', contactPhone: '' })
const rules = reactive({
  locationName: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
  cityId: [{ required: true, message: '请选择城市', trigger: 'change' }]
})

// 【修复9】使用 nextTick 清空表单并完全重置对象属性
const handleAdd = () => { 
  dialogTitle.value = '新增销售地点'
  dialogVisible.value = true 
  nextTick(() => {
    if (formRef.value) formRef.value.resetFields()
    form.id = null
    Object.assign(form, { locationName: '', cityId: null, address: '', contactPerson: '', contactPhone: '' })
  })
}

// 【修复10】使用 nextTick 延迟赋值，防止污染 Element UI 表单的初始状态
const handleEdit = (row) => { 
  dialogTitle.value = '修改销售地点'
  dialogVisible.value = true 
  nextTick(() => {
    Object.assign(form, row)
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`删除地点【${row.locationName}】?`, '警告', { type: 'warning' }).then(async () => {
    await deleteLocation(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}

const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
  // 防御性拦截，防止 formRef 没挂载时报错
  if (!formRef.value) return
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) await updateLocation(form)
        else await addLocation(form)
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