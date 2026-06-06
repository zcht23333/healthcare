<template>
  <div class="drug-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="药品名称">
            <el-input v-model="queryParams.drugName" placeholder="请输入药品名称" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增药品</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="drugName" label="药品名称" min-width="150" />
        <el-table-column prop="companyName" label="生产企业(医药公司)" min-width="180" show-overflow-tooltip />
        <el-table-column prop="approvalNumber" label="国药准字(批准文号)" min-width="150" />
        <el-table-column prop="specification" label="规格" width="120" align="center" />
        <el-table-column prop="unitPrice" label="单价(元)" width="100" align="center">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">￥{{ scope.row.unitPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
        
        <el-table-column label="药品图片" width="100" align="center">
          <template #default="scope">
            <el-image 
              style="width: 50px; height: 50px; border-radius: 4px;"
              :src="scope.row.drugImage" 
              :preview-src-list="[scope.row.drugImage]"
              preview-teleported
              fit="cover"
            >
              <template #error>
                <div style="font-size: 12px; color: #909399; line-height: 50px; background: #f5f7fa;">无图片</div>
              </template>
            </el-image>
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
        <el-form-item label="药品名称" prop="drugName">
          <el-input v-model="form.drugName" placeholder="请输入药品名称" />
        </el-form-item>
        
        <el-form-item label="生产企业" prop="companyId">
          <el-select v-model="form.companyId" placeholder="请选择所属医药公司" style="width: 100%">
            <el-option
              v-for="item in companyOptions"
              :key="item.id"
              :label="item.companyName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="批准文号" prop="approvalNumber">
          <el-input v-model="form.approvalNumber" placeholder="请输入批准文号" />
        </el-form-item>
        <el-form-item label="药品规格" prop="specification">
          <el-input v-model="form.specification" placeholder="请输入药品规格" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="form.unitPrice" :precision="2" :step="1" :min="0" style="width: 100%" />
        </el-form-item>

        <el-form-item label="药品图片" prop="drugImage">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="form.drugImage" :src="form.drugImage" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
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
// 【修复9】引入 nextTick，处理表单清空与延迟赋值
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { getDrugList, addDrug, updateDrug, deleteDrug } from '@/api/drug'
import { getCompanyList } from '@/api/company'
import { Message as ElMessage, MessageBox as ElMessageBox } from 'element-ui'

// --- 表格查询 ---
const queryParams = reactive({ pageNum: 1, pageSize: 10, drugName: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const companyOptions = ref([]) 

onMounted(() => {
  fetchList()
  fetchCompanyOptions()
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getDrugList(queryParams)
    tableData.value = res.list
    total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}

const fetchCompanyOptions = async () => {
  try {
    const res = await getCompanyList({ pageNum: 1, pageSize: 1000 })
    companyOptions.value = res.list
  } catch (error) { console.error('获取医药公司列表失败', error) }
}

const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.drugName = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

// --- 弹窗控制 ---
const dialogVisible = ref(false)
const dialogTitle = ref('新增药品')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  drugName: '',
  companyId: null,
  approvalNumber: '',
  specification: '',
  unitPrice: 0.00,
  drugImage: '' 
})

const rules = reactive({
  drugName: [{ required: true, message: '药品名称不能为空', trigger: 'blur' }],
  companyId: [{ required: true, message: '请选择生产企业', trigger: 'change' }],
  unitPrice: [{ required: true, message: '单价不能为空', trigger: 'blur' }]
})

// 【修复10】使用 nextTick 清除表单和残留数据
const handleAdd = () => {
  dialogTitle.value = '新增药品'
  dialogVisible.value = true
  nextTick(() => {
    if (formRef.value) formRef.value.resetFields()
    form.id = null
    // 强制重置确保上次修改的数据被彻底清除
    Object.assign(form, { drugName: '', companyId: null, approvalNumber: '', specification: '', unitPrice: 0.00, drugImage: '' })
  })
}

// 【修复11】使用 nextTick 延迟赋值，避免破坏初始表单状态
const handleEdit = (row) => {
  dialogTitle.value = '修改药品'
  dialogVisible.value = true
  nextTick(() => {
    Object.assign(form, row)
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`此操作将删除药品【${row.drugName}】, 是否继续?`, '警告', { type: 'warning' }).then(async () => {
    await deleteDrug(row.id)
    ElMessage.success('删除成功！')
    handleSearch()
  }).catch(() => {})
}

const handleCloseDialog = () => {
  if (formRef.value) formRef.value.resetFields()
  form.id = null
}

const submitForm = () => {
  if (!formRef.value) return
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) await updateDrug(form)
        else await addDrug(form)
        ElMessage.success(form.id ? '修改成功' : '新增成功')
        dialogVisible.value = false
        fetchList()
      } catch (error) { console.error(error) } finally { submitLoading.value = false }
    }
  })
}

const uploadHeaders = computed(() => {
  return { Authorization: localStorage.getItem('token') }
})

const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    ElMessage.success('图片上传成功！')
    form.drugImage = res.data 
  } else {
    ElMessage.error(res.message || '上传失败')
  }
}

const beforeUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
  const isLt2M = file.size / 1024 / 1024 < 5 
  if (!isImage) {
    ElMessage.error('药品图片只能是 JPG/PNG/GIF 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 5MB!')
  }
  return isImage && isLt2M
}
</script>

<style scoped>
.header-action { display: flex; justify-content: space-between; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }

/* 药品图片上传框样式 */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  line-height: 120px;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
}
</style>