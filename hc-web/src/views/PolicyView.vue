<template>
  <div class="policy-container">
    <el-card class="box-card">
      <div class="header-action">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="政策标题">
            <el-input v-model="queryParams.policyTitle" placeholder="请输入标题" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-btn">
          <el-button type="primary" icon="Plus" @click="handleAdd">发布新政策</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%" :header-cell-style="{background:'#f5f7fa', color:'#606266'}">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="policyTitle" label="医保政策标题" min-width="300" show-overflow-tooltip>
          <template #default="scope">
            <span style="font-weight: bold; color: #409EFF;">{{ scope.row.policyTitle }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="publishDate" label="发布日期" width="150" align="center" />
        <el-table-column prop="createTime" label="录入时间" width="180" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleEdit(scope.row)">编辑内容</el-button>
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="900px" top="5vh" @close="handleCloseDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="政策标题" prop="policyTitle">
              <el-input v-model="form.policyTitle" placeholder="请输入官方政策文件标题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发布日期" prop="publishDate">
              <el-date-picker v-model="form.publishDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="政策正文" prop="policyContent">
          <div style="border: 1px solid #ccc; width: 100%; z-index: 100;">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              style="height: 400px; overflow-y: hidden;"
              v-model="form.policyContent"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
            />
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
import { ref, reactive, onMounted, shallowRef, onBeforeUnmount } from 'vue'
import { getPolicyList, addPolicy, updatePolicy, deletePolicy } from '@/api/policy'
import { ElMessage, ElMessageBox } from 'element-plus'

// 引入 WangEditor 的样式和组件
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

// --- 表格查询 ---
const queryParams = reactive({ pageNum: 1, pageSize: 10, policyTitle: '' })
const loading = ref(false)
const tableData = ref([])
const total = ref(0)

onMounted(() => fetchList())
const fetchList = async () => {
  loading.value = true
  try {
    const res = await getPolicyList(queryParams)
    tableData.value = res.list
    total.value = res.total
  } catch (error) { console.error(error) } finally { loading.value = false }
}
const handleSearch = () => { queryParams.pageNum = 1; fetchList() }
const resetQuery = () => { queryParams.policyTitle = ''; handleSearch() }
const handleSizeChange = (val) => { queryParams.pageSize = val; fetchList() }
const handleCurrentChange = (val) => { queryParams.pageNum = val; fetchList() }

// --- 弹窗与表单 ---
const dialogVisible = ref(false)
const dialogTitle = ref('发布医保政策')
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  policyTitle: '',
  publishDate: '',
  policyContent: '' // 绑定的 HTML 富文本源码
})

const rules = reactive({
  policyTitle: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  publishDate: [{ required: true, message: '请选择发布日期', trigger: 'change' }],
  policyContent: [{ required: true, message: '正文内容不能为空', trigger: 'blur' }]
})

const handleAdd = () => { dialogTitle.value = '发布医保政策'; form.id = null; dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '修改医保政策'; Object.assign(form, row); dialogVisible.value = true }
const handleDelete = (row) => {
  ElMessageBox.confirm(`删除政策【${row.policyTitle}】?`, '警告', { type: 'warning' }).then(async () => {
    await deletePolicy(row.id); ElMessage.success('删除成功！'); handleSearch()
  }).catch(() => {})
}
const handleCloseDialog = () => { if (formRef.value) formRef.value.resetFields(); form.id = null }

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      // 拦截空内容
      if (form.policyContent === '<p><br></p>') { ElMessage.warning('正文内容不能为空'); return }
      submitLoading.value = true
      try {
        if (form.id) await updatePolicy(form)
        else await addPolicy(form)
        ElMessage.success(form.id ? '修改成功' : '发布成功')
        dialogVisible.value = false
        fetchList()
      } catch (error) { console.error(error) } finally { submitLoading.value = false }
    }
  })
}

// ================= WangEditor 专属配置区域 =================
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const mode = 'default' // 或 'simple'
const toolbarConfig = {} // 工具栏配置（全量）

// 编辑器配置：深度对接 Java 后端的图片上传接口
const editorConfig = {
  placeholder: '请输入政策正文内容，支持直接粘贴截图并自动上传...',
  MENU_CONF: {
    uploadImage: {
      server: 'http://localhost:8080/upload', // 对接你的后端上传接口
      fieldName: 'file', // SpringMVC 中 MultipartFile 参数的名字
      // 必须带上 Token，否则会被后端 AuthInterceptor 拦截打回 401
      headers: { Authorization: localStorage.getItem('token') },
      // 自定义插入图片逻辑：获取后端返回的 res.data (图片URL)，交给编辑器渲染
      customInsert(res, insertFn) {
        if (res.code === 200) {
          insertFn(res.data) // 将拿到的 http://... 图片地址插入到编辑器
        } else {
          ElMessage.error('图片上传失败：' + res.message)
        }
      }
    }
  }
}

// 记录 editor 实例
const handleCreated = (editor) => { editorRef.value = editor }

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<style scoped>
.header-action { display: flex; justify-content: space-between; margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>