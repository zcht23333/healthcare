<template>
  <div class="pharma-policy-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="政策标题">
            <el-input v-model="queryParams.policyTitle" placeholder="请输入政策标题" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">发布公司政策</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="companyName" label="所属医药公司" min-width="180">
          <template #default="scope">
            {{ scope.row.companyName || '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="policyTitle" label="政策标题" min-width="250" show-overflow-tooltip>
          <template #default="scope">
            <span style="font-weight: bold; color: #409EFF;">{{ scope.row.policyTitle }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="生效开始日期" width="130" align="center" />
        <el-table-column prop="endDate" label="生效结束日期" width="130" align="center" />
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" top="5vh" @close="handleCloseDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属医药公司" prop="companyId">
              <el-select v-model="form.companyId" placeholder="请选择医药公司" style="width: 100%">
                <el-option v-for="item in companyOptions" :key="item.id" :label="item.companyName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政策标题" prop="policyTitle">
              <el-input v-model="form.policyTitle" placeholder="请输入政策标题" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" value-format="yyyy-MM-dd" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" value-format="yyyy-MM-dd" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="政策内容" prop="policyContent">
          <div style="border: 1px solid #ccc; width: 100%; z-index: 100;">
            <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
            <Editor style="height: 300px; overflow-y: hidden;" v-model="form.policyContent" :defaultConfig="editorConfig" :mode="mode" @onCreated="handleCreated" />
          </div>
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
// 【修复8】引入 nextTick 解决表单数据残留问题
import { ref, reactive, onMounted, shallowRef, onBeforeUnmount, nextTick } from 'vue'
import { getPharmaPolicyList, addPharmaPolicy, updatePharmaPolicy, deletePharmaPolicy } from '@/api/pharmaPolicy'
import { getCompanyList } from '@/api/company'
import { Message as ElMessage, MessageBox as ElMessageBox } from 'element-ui'

import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const queryParams = reactive({ pageNum: 1, pageSize: 10, policyTitle: '' })
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
    const res = await getPharmaPolicyList(queryParams)
    tableData.value = res.list; total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}

const fetchCompanyOptions = async () => {
  try {
    const res = await getCompanyList({ pageNum: 1, pageSize: 1000 })
    companyOptions.value = res.list
  } catch (error) { console.error(error) }
}

const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.policyTitle = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

const dialogVisible = ref(false)
const dialogTitle = ref('发布公司政策')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({ id: null, companyId: null, policyTitle: '', startDate: '', endDate: '', policyContent: '' })
const rules = reactive({
  companyId: [{ required: true, message: '请选择医药公司', trigger: 'change' }],
  policyTitle: [{ required: true, message: '政策标题不能为空', trigger: 'blur' }],
  policyContent: [{ required: true, message: '政策内容不能为空', trigger: 'blur' }]
})

// 【修复9】使用 nextTick 清空表单，并完全重置所有字段的值
const handleAdd = () => { 
  dialogTitle.value = '发布公司政策'
  dialogVisible.value = true 
  nextTick(() => {
    if (formRef.value) formRef.value.resetFields()
    form.id = null
    Object.assign(form, { companyId: null, policyTitle: '', startDate: '', endDate: '', policyContent: '' })
  })
}

// 【修复10】使用 nextTick 延迟赋值，防止污染初始状态
const handleEdit = (row) => { 
  dialogTitle.value = '修改公司政策'
  dialogVisible.value = true 
  nextTick(() => {
    Object.assign(form, row)
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`删除政策【${row.policyTitle}】?`, '警告', { type: 'warning' }).then(async () => {
    await deletePharmaPolicy(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}

const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
  if (!formRef.value) return
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.policyContent === '<p><br></p>') { ElMessage.warning('内容不能为空'); return }
      submitLoading.value = true
      try {
        if (form.id) await updatePharmaPolicy(form)
        else await addPharmaPolicy(form)
        ElMessage.success(form.id ? '修改成功' : '发布成功')
        dialogVisible.value = false
        fetchList()
      } catch (error) { console.error(error) } finally { submitLoading.value = false }
    }
  })
}

// ================= WangEditor 配置 =================
const editorRef = shallowRef()
const mode = 'default'
const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入医药公司的具体政策（如折扣、返利规则），支持直接粘贴图片...',
  MENU_CONF: {
    uploadImage: {
      server: 'http://localhost:8080/upload',
      fieldName: 'file',
      headers: { Authorization: localStorage.getItem('token') },
      customInsert(res, insertFn) {
        if (res.code === 200) insertFn(res.data)
        else ElMessage.error('图片上传失败')
      }
    }
  }
}
const handleCreated = (editor) => { editorRef.value = editor }
onBeforeUnmount(() => { const editor = editorRef.value; if (editor == null) return; editor.destroy() })
</script>

<style scoped>
.header-action { display: flex; justify-content: space-between; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>