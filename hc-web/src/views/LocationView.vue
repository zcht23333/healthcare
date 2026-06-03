<template>
  <div class="location-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="地点名称">
            <el-input v-model="queryParams.locationName" placeholder="如：某某大药房" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="success" icon="Plus" @click="handleAdd">新增销售地点</el-button>
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
            <el-button type="primary" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" @close="handleCloseDialog">
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
import { ref, reactive, onMounted } from 'vue'
import { getLocationList, addLocation, updateLocation, deleteLocation } from '@/api/location'
import { getCityList } from '@/api/city' // 引入获取城市列表的方法
import { ElMessage, ElMessageBox } from 'element-plus'

const queryParams = reactive({ pageNum: 1, pageSize: 10, locationName: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const cityOptions = ref([]) // 城市下拉框数据源

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

// 获取所有城市供下拉框选择
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

const handleAdd = () => { dialogTitle.value = '新增销售地点'; form.id = null; dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '修改销售地点'; Object.assign(form, row); dialogVisible.value = true }
const handleDelete = (row) => {
  ElMessageBox.confirm(`删除地点【${row.locationName}】?`, '警告', { type: 'warning' }).then(async () => {
    await deleteLocation(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}
const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
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