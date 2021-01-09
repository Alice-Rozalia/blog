<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>类型管理</el-breadcrumb-item>
      <el-breadcrumb-item>分类列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-button type="primary" size="small" @click="addDialogVisible = true">添加分类</el-button>
      <!-- Tag标签列表区域 -->
      <el-table :data="typelist" border stripe>
        <el-table-column type="index" label="序号" width="70"></el-table-column>
        <el-table-column prop="name" label="分类名称"></el-table-column>
        <el-table-column label="创建时间">
          <template slot-scope="scope">{{scope.row.createTime | dateFormat}}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editType(scope.row)">修改
            </el-button>
            <!-- 删除按钮 -->
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteTypeById(scope.row.id)">删除
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

    <!-- 修改分类 -->
    <el-dialog title="修改分类" :visible.sync="editDialogVisible" width="50%">
      <el-form label-position="right" :rules="comRules" ref="editFormRef" label-width="80px" :model="editCategoryForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="editCategoryForm.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editCat">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 新增分类 -->
    <el-dialog title="新增分类" :visible.sync="addDialogVisible" width="50%">
      <el-form label-position="right" :rules="comRules" ref="addFormRef" label-width="80px" :model="addCategoryForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addCategoryForm.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCat">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { findCategoriesApi, deleteCategoryApi, editCategoryApi, addCategoryApi } from '@/api/type'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8
        },
        total: 0,
        typelist: [],
        editDialogVisible: false,
        addDialogVisible: false,
        editCategoryForm: {},
        addCategoryForm: {
          name: ''
        },
        comRules: {
          name: [{required: true,
            message: '请输入分类名称！',
            trigger: 'blur'}]
        }
      }
    },
    created() {
      this.getCategories()
    },
    methods: {
      async getCategories() {
        const {
          data
        } = await findCategoriesApi(this.queryInfo)
        this.typelist = data.data.categories.rows
        this.total = data.data.categories.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getCategories()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getCategories()
      },
      // 点击删除按钮，删除留言
      async deleteTypeById(id) {
        const res = await this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch(err => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const {
          data
        } = await deleteCategoryApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.getCategories()
      },
      editType(data) {
        this.editDialogVisible = true
        this.editCategoryForm = { ...data }
      },
      async editCat() {
        this.$refs.editFormRef.validate(async valid => {
          if (!valid) return;
          const { data } = await editCategoryApi(this.editCategoryForm)
          if (!data.success) return this.$message.error(data.message)
          this.$message.success(data.message)
          this.editDialogVisible = false
          this.getCategories()
          this.$refs.editFormRef.resetFields()
        })
      },
      async addCat() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const { data } = await addCategoryApi(this.addCategoryForm)
          if (!data.success) return this.$message.error(data.message)
          this.$message.success(data.message)
          this.addDialogVisible = false
          this.getCategories()
          this.$refs.addFormRef.resetFields()
        })
      }
    }
  }
</script>