<template>
  <div class="company-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="公司名称">
            <el-input 
              v-model="queryParams.companyName" 
              placeholder="请输入公司名称" 
              clearable 
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        
        <div class="action-btn">
          <el-button type="primary" icon="Plus" @click="handleAdd">新增医药公司</el-button>
        </div>
      </div>

      <el-table 
        v-loading="loading" 
        :data="tableData" 
        border 
        stripe 
        style="width: 100%"
        :header-cell-style="{background:'#f5f7fa', color:'#606266'}"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="companyName" label="公司名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="licenseNo" label="统一社会信用代码" min-width="160" />
        <el-table-column prop="contactPerson" label="联系人" width="100" align="center" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" align="center" />
        <el-table-column prop="createTime" label="入驻时间" width="160" align="center" />
        
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

    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      @close="handleCloseDialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
        <el-form-item label="公司名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入公司全称" />
        </el-form-item>
        <el-form-item label="统一社会信用代码" prop="licenseNo">
          <el-input v-model="form.licenseNo" placeholder="10位营业执照号码" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入手机或固话" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入公司详细地址" />
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
// 【引入全部四个API】以及 ElMessageBox 用于删除二次确认
import { getCompanyList, addCompany, updateCompany, deleteCompany } from '@/api/company' 
import { ElMessage, ElMessageBox } from 'element-plus'

const queryParams = reactive({ pageNum: 1, pageSize: 10, companyName: '' })
const loading = ref(false) 
const tableData = ref([])  
const total = ref(0)       

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getCompanyList(queryParams)
    tableData.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取列表失败', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchList())
const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.companyName = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

// --- 弹窗与表单控制 ---
const dialogVisible = ref(false)
const dialogTitle = ref('新增医药公司') // 动态标题
const submitLoading = ref(false)
const formRef = ref(null)

// 【核心】：表单数据新增了一个 id 字段，用来区分是新增还是修改
const form = reactive({
  id: null, 
  companyName: '',
  licenseNo: '',
  contactPerson: '',
  contactPhone: '',
  address: ''
})

const rules = reactive({
  companyName: [{ required: true, message: '公司名称不能为空', trigger: 'blur' }],
  licenseNo: [
    { required: true, message: '统一社会信用代码不能为空', trigger: 'blur' },
    { min: 10, max: 10, message: '信用代码应为10位', trigger: 'blur' }
  ],
  contactPerson: [{ required: true, message: '联系人不能为空', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '联系电话不能为空', trigger: 'blur' }]
})

// 点击新增
const handleAdd = () => {
  dialogTitle.value = '新增医药公司'
  form.id = null // 确保没有ID
  dialogVisible.value = true
}

// 【新增】：点击编辑
const handleEdit = (row) => {
  dialogTitle.value = '修改医药公司'
  // 将表格当前行的数据直接浅拷贝给表单
  Object.assign(form, row)
  dialogVisible.value = true
}

// 【新增】：点击删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`此操作将删除公司【${row.companyName}】, 是否继续?`, '高危操作警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    // 调用删除 API
    await deleteCompany(row.id)
    ElMessage.success('删除成功！')
    handleSearch() // 删除后自动刷新回到第一页
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 弹窗关闭时：清空校验痕迹和主键 ID
const handleCloseDialog = () => {
  if (formRef.value) formRef.value.resetFields()
  form.id = null // 必须清空 ID，防止下次点击新增变成了修改
}

// 提交表单 (融合了新增和修改)
const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true 
      try {
        if (form.id) {
          // 有 ID 说明是修改
          await updateCompany(form)
          ElMessage.success('修改信息成功！')
        } else {
          // 没 ID 说明是新增
          await addCompany(form)
          ElMessage.success('新增公司成功！')
        }
        dialogVisible.value = false 
        fetchList() // 刷新当前页面的数据
      } catch (error) {
        console.error('提交失败', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}
</script>

<style scoped>
.company-container { width: 100%; }
.header-action { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>