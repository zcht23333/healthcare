<template>
  <div class="doctor-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="医生姓名">
            <el-input v-model="queryParams.realName" placeholder="请输入姓名模糊查询" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="success" icon="Plus" @click="handleAdd">新增医生</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="登录账号(工号)" min-width="150" />
        <el-table-column prop="realName" label="医生姓名" width="120" align="center">
          <template #default="scope">
            <span style="font-weight: bold;">{{ scope.row.realName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="hospitalName" label="所属医院" min-width="180" show-overflow-tooltip />
        <el-table-column prop="department" label="所属科室" width="120" align="center" />
        <el-table-column prop="phone" label="联系电话" width="140" align="center" />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="warning" link icon="Key" @click="handleResetPwd(scope.row)">重置密码</el-button>
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
        <el-form-item label="登录账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入工号或手机号作为账号" :disabled="form.id != null" />
        </el-form-item>
        <el-form-item label="医生姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入医生真实姓名" />
        </el-form-item>
        <el-form-item label="所属医院" prop="hospitalName">
          <el-input v-model="form.hospitalName" placeholder="请输入所属医院名称" />
        </el-form-item>
        <el-form-item label="所属科室" prop="department">
          <el-input v-model="form.department" placeholder="如：内科、外科" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入医生联系电话" />
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
import { getDoctorList, addDoctor, updateDoctor, deleteDoctor, resetDoctorPwd } from '@/api/doctor'
import { ElMessage, ElMessageBox } from 'element-plus'

const queryParams = reactive({ pageNum: 1, pageSize: 10, realName: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)

onMounted(() => fetchList())
const fetchList = async () => {
  loading.value = true
  try {
    const res = await getDoctorList(queryParams)
    tableData.value = res.list; total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}
const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.realName = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

const dialogVisible = ref(false)
const dialogTitle = ref('新增医生')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ id: null, username: '', realName: '', hospitalName: '', department: '', phone: '' })
const rules = reactive({
  username: [{ required: true, message: '登录账号不能为空', trigger: 'blur' }],
  realName: [{ required: true, message: '真实姓名不能为空', trigger: 'blur' }]
})

const handleAdd = () => { dialogTitle.value = '新增医生'; form.id = null; dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '修改医生信息'; Object.assign(form, row); dialogVisible.value = true }
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除医生【${row.realName}】的信息吗?`, '警告', { type: 'warning' }).then(async () => {
    await deleteDoctor(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}

// 核心功能：重置密码
const handleResetPwd = (row) => {
  ElMessageBox.confirm(`确定要将医生【${row.realName}】的密码重置为 123456 吗？`, '高危操作提醒', {
    confirmButtonText: '强制重置',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    await resetDoctorPwd(row.id)
    ElMessage.success(`医生【${row.realName}】的密码已成功重置为 123456！`)
  }).catch(() => {})
}

const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) await updateDoctor(form)
        else {
          await addDoctor(form)
          ElMessage.success('新增成功，默认登录密码为 123456')
        }
        dialogVisible.value = false; fetchList()
      } catch (error) { console.error(error) } finally { submitLoading.value = false }
    }
  })
}
</script>

<style scoped>
.header-action { display: flex; justify-content: space-between; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>