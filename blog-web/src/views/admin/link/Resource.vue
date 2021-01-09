<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>资源链接</el-breadcrumb-item>
      <el-breadcrumb-item>资源列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-button type="primary" size="small" @click="addResourceDialogVisible = true">添加资源链接</el-button>
      <!-- Tag标签列表区域 -->
      <el-table :data="list" border stripe>
        <el-table-column type="index" label="序号" width="70"></el-table-column>
        <el-table-column prop="name" label="资源名称" align="center"></el-table-column>
        <el-table-column prop="link" label="链接地址" align="center"></el-table-column>
        <el-table-column prop="code" label="提取码" align="center" width="50"></el-table-column>
        <el-table-column prop="type" label="资源分类" align="center"></el-table-column>
        <el-table-column label="图片" align="center">
          <template slot-scope="scope">
            <img :src="scope.row.image" style="width: 80px" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editResourceById(scope.row)" />
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteById(scope.row.id)" />
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryInfo.page" :page-sizes="[3, 5, 8, 12]" :page-size="queryInfo.rows"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </el-card>

    <!-- 修改资源链接的对话框 -->
    <el-dialog title="修改资源链接" :visible.sync="editResourceDialogVisible" width="50%">
      <el-form label-position="right" label-width="80px" :model="editForm">
        <el-form-item label="名称">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="editForm.type"></el-input>
        </el-form-item>
        <el-form-item label="链接">
          <el-input v-model="editForm.link"></el-input>
        </el-form-item>
        <el-form-item label="提取码">
          <el-input v-model="editForm.code"></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload action="http://8.129.237.3/api/v1/pri/alioss/upload" :on-remove="deleteEditImg" :on-error="uploadError"
            :file-list="editImg" :on-success="editImgSuccess" :headers="headerObj" :limit="1" list-type="picture">
            <el-button size="mini" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editResourceDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editResource">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 新增资源链接的对话框 -->
    <el-dialog title="新增资源链接" :visible.sync="addResourceDialogVisible" width="50%">
      <el-form label-position="right" ref="addRuleFormRef" :rules="addResourceRules" label-width="80px"
        :model="addResourceForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addResourceForm.name"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input v-model="addResourceForm.type"></el-input>
        </el-form-item>
        <el-form-item label="链接" prop="link">
          <el-input v-model="addResourceForm.link"></el-input>
        </el-form-item>
        <el-form-item label="提取码" prop="code">
          <el-input v-model="addResourceForm.code"></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload action="http://8.129.237.3/api/v1/pri/alioss/upload" :on-remove="deleteEditImg" :on-error="uploadError"
            :file-list="addImg" :on-success="addImgSuccess" :headers="headerObj" :limit="1" list-type="picture">
            <el-button size="mini" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addResourceDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addResource">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
    findResourceApi,
    deleteResourceApi,
    editResourceApi,
    addResourceApi,
  } from '@/api/link'
  import { deleteImgApi } from '@/api/file'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 5
        },
        total: 0,
        list: [],
        editForm: {},
        addResourceForm: {
          name: '',
          link: '',
          type: '',
          code: '',
          image: ''
        },
        editResourceDialogVisible: false,
        addResourceDialogVisible: false,
        addResourceRules: {
          name: [{
            required: true,
            message: '请输入友链名称！',
            trigger: 'blur'
          }],
          link: [{
            required: true,
            message: '请输入链接地址！',
            trigger: 'blur'
          }],
          code: [{
            required: true,
            message: '请输入提取码！',
            trigger: 'blur'
          }],
          type: [{
            required: true,
            message: '请输入资源类型！',
            trigger: 'blur'
          }]
        },
        editImg: [{ name: '图片', url: '' }],
        addImg: [],
        // 图片上传组件的请求头
        headerObj: {
          Authorization: window.sessionStorage.getItem('token')
        }
      }
    },
    created() {
      this.getResourceList()
    },
    methods: {
      async getResourceList() {
        const {
          data
        } = await findResourceApi(this.queryInfo)
        this.list = data.data.resources.rows
        this.total = data.data.resources.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getResourceList()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getResourceList()
      },
      // 点击删除按钮，删除留言
      async deleteById(id) {
        const res = await this.$confirm('此操作将永久删除该资源链接, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch((err) => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const {
          data
        } = await deleteResourceApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success("删除成功！")

        this.getResourceList()
      },
      // 显示修改资源链接
      editResourceById(data) {
        this.editResourceDialogVisible = true
        this.editForm = {
          ...data
        }
        this.editImg[0].url = this.editForm.image
      },
      // 修改资源链接
      async editResource() {
        const {
          data
        } = await editResourceApi(this.editForm)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success("修改成功！")
        this.getResourceList()
        this.editResourceDialogVisible = false
      },
      // 新增资源链接
      async addResource() {
        this.$refs.addRuleFormRef.validate(async valid => {
          if (!valid) return;
          const {
            data
          } = await addResourceApi(this.addResourceForm)
          if (!data.success) return this.$message.error(data.message)
          this.$message.success("添加成功！")
          this.getResourceList()
          this.addResourceDialogVisible = false
          this.$refs.addRuleFormRef.resetFields()
        })
      },
      // 修改资源链接的图片
      editImgSuccess(response) {
        this.editForm.image = response.data.url
      },
      // 删除图片
      async deleteEditImg(file) {
        const {
          data
        } = await deleteImgApi(file.url)
        if (data.success) this.$message.success('该图片已被删除！')
      },
      uploadError() {
        this.$message.error('上传失败！')
      },
      addImgSuccess(response) {
        this.addResourceForm.image = response.data.url
      }
    }
  }
</script>