<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>资源链接</el-breadcrumb-item>
      <el-breadcrumb-item>友链列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-button type="primary" size="small" @click="addLinkDialogVisible = true">添加友情链接</el-button>

      <!-- Tag标签列表区域 -->
      <el-table :data="linklist" border stripe>
        <el-table-column type="index" label="序号" width="70"></el-table-column>
        <el-table-column prop="name" label="友链名称" align="center"></el-table-column>
        <el-table-column prop="link" label="链接地址" align="center"></el-table-column>
        <el-table-column label="图片" align="center">
          <template slot-scope="scope">
            <img :src="scope.row.image" style="width: 80px" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editLinkById(scope.row)">修改
            </el-button>
            <!-- 删除按钮 -->
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteLinkById(scope.row.id)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryInfo.page" :page-sizes="[3, 5, 8, 12]" :page-size="queryInfo.rows"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </el-card>

    <!-- 修改友情链接的对话框 -->
    <el-dialog title="修改友情链接" :visible.sync="editLinkDialogVisible" width="50%">
      <el-form label-position="right" label-width="80px" :model="editLinkForm">
        <el-form-item label="名称">
          <el-input v-model="editLinkForm.name"></el-input>
        </el-form-item>
        <el-form-item label="链接">
          <el-input v-model="editLinkForm.link"></el-input>
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
        <el-button @click="editLinkDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editLink">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 添加友情链接的对话框 -->
    <el-dialog title="添加友情链接" :visible.sync="addLinkDialogVisible" width="50%">
      <el-form label-position="right" :rules="addLinkRules" ref="addRuleFormRef" label-width="80px"
        :model="addLinkForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addLinkForm.name"></el-input>
        </el-form-item>
        <el-form-item label="链接" prop="link">
          <el-input v-model="addLinkForm.link"></el-input>
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
        <el-button @click="addLinkDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addLink">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
    findFriendlyLinkApi,
    deleteFriendlyLinkApi,
    editFriendlyLinkApi,
    addFriendlyLinkApi
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
        linklist: [],
        // 控制修改友情链接的对话框
        editLinkDialogVisible: false,
        addLinkDialogVisible: false,
        editLinkForm: {},
        addLinkForm: {
          name: '',
          link: '',
          image: ''
        },
        addLinkRules: {
          name: [{
            required: true,
            message: '请输入友链名称！',
            trigger: 'blur'
          }],
          link: [{
            required: true,
            message: '请输入链接地址！',
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
      this.getLinkList()
    },
    methods: {
      async getLinkList() {
        const {
          data
        } = await findFriendlyLinkApi(this.queryInfo)
        this.linklist = data.data.links.rows
        this.total = data.data.links.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getLinkList()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getLinkList()
      },
      // 点击删除按钮，删除留言
      async deleteLinkById(id) {
        const res = await this.$confirm('此操作将永久删除该友情链接, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch((err) => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const {
          data
        } = await deleteFriendlyLinkApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success("删除成功！")

        this.getLinkList()
      },
      // 显示修改友情链接对话框
      editLinkById(data) {
        this.editLinkDialogVisible = true
        this.editLinkForm = {
          ...data
        }
        this.editImg[0].url = this.editLinkForm.image
      },
      async editLink() {
        const {
          data
        } = await editFriendlyLinkApi(this.editLinkForm)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success("修改成功！")
        this.getLinkList()
        this.editLinkDialogVisible = false
      },
      async addLink() {
        this.$refs.addRuleFormRef.validate(async valid => {
          if (!valid) return;
          const {
            data
          } = await addFriendlyLinkApi(this.addLinkForm)
          if (!data.success) return this.$message.error(data.message)
          this.$message.success("添加成功！")
          this.getLinkList()
          this.addLinkDialogVisible = false
          this.$refs.addRuleFormRef.resetFields()
          this.addImg = []
        })
      },
      // 修改友情链接的图片
      editImgSuccess(response) {
        this.editLinkForm.image = response.data.url
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
        this.addLinkForm.image = response.data.url
      }
    }
  }
</script>