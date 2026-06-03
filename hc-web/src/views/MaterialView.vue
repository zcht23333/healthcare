<template>
  <div class="material-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="材料名称">
            <el-input v-model="queryParams.materialName" placeholder="请输入材料名称模糊查询" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="success" icon="Plus" @click="handleAdd">新增材料</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="materialName" label="材料名称" min-width="180" show-overflow-tooltip>
          <template #default="scope">
            <span style="font-weight: bold;">{{ scope.row.materialName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="materialType" label="材料类型" width="150" align="center">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.materialType)">{{ scope.row.materialType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="材料说明" min-width="250" show-overflow-tooltip />
        <el-table-column prop="createTime" label="录入时间" width="180" align="center" />
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
        <el-form-item label="材料名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="如：首营企业审批表" />
        </el-form-item>
        <el-form-item label="材料类型" prop="materialType">
          <el-select v-model="form.materialType" placeholder="请选择类型" style="width: 100%">
            <el-option label="资质文件" value="资质文件" />
            <el-option label="合同附件" value="合同附件" />
            <el-option label="审批表单" value="审批表单" />
            <el-option label="其他材料" value="其他材料" />
          </el-select>
        </el-form-item>
        <el-form-item label="材料说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入材料用途或相关说明" />
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
import { getMaterialList, addMaterial, updateMaterial, deleteMaterial } from '@/api/material'
import { ElMessage, ElMessageBox } from 'element-plus'

const queryParams = reactive({ pageNum: 1, pageSize: 10, materialName: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)

onMounted(() => fetchList())
const fetchList = async () => {
  loading.value = true
  try {
    const res = await getMaterialList(queryParams)
    tableData.value = res.list; total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}
const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.materialName = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

const dialogVisible = ref(false)
const dialogTitle = ref('新增材料')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ id: null, materialName: '', materialType: '', description: '' })
const rules = reactive({
  materialName: [{ required: true, message: '材料名称不能为空', trigger: 'blur' }],
  materialType: [{ required: true, message: '请选择材料类型', trigger: 'change' }]
})

const handleAdd = () => { dialogTitle.value = '新增材料'; form.id = null; dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '修改材料信息'; Object.assign(form, row); dialogVisible.value = true }
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除材料【${row.materialName}】吗?`, '警告', { type: 'warning' }).then(async () => {
    await deleteMaterial(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}
const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) await updateMaterial(form)
        else await addMaterial(form)
        ElMessage.success(form.id ? '修改成功' : '新增成功')
        dialogVisible.value = false; fetchList()
      } catch (error) { console.error(error) } finally { submitLoading.value = false }
    }
  })
}

// 根据材料类型渲染不同颜色的 Tag
const getTagType = (type) => {
  const map = { '资质文件': 'danger', '合同附件': 'warning', '审批表单': 'success', '其他材料': 'info' }
  return map[type] || ''
}
</script>

<style scoped>
.header-action { display: flex; justify-content: space-between; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>