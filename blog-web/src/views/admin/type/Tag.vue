<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>类型管理</el-breadcrumb-item>
      <el-breadcrumb-item>标签列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-button type="primary" size="small" @click="addDialogVisible = true">添加标签</el-button>
      <!-- Tag标签列表区域 -->
      <el-table :data="taglist" border stripe>
        <el-table-column type="index" label="序号" width="70"></el-table-column>
        <el-table-column prop="name" label="Tag 标签名称" align="center"></el-table-column>
        <el-table-column prop="color" label="颜色" align="center" width="60">
          <template slot-scope="scope">
            <div class="tag-color" :style="{ 'background': scope.row.color }"></div>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">{{scope.row.createTime | dateFormat}}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="deleteTagById(scope.row.id)">修改
            </el-button>
            <!-- 删除按钮 -->
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteTagById(scope.row.id)">删除
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

    <!-- 新增标签 -->
    <el-dialog title="新增标签" :visible.sync="addDialogVisible" width="50%">
      <el-form label-position="right" :rules="comRules" ref="addFormRef" label-width="80px" :model="addTagForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addTagForm.name"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="color">
          <el-select v-model="addTagForm.color" placeholder="请选择标签颜色">
            <el-option v-for="item in colorlist" :key="item.value" :label="item.label" :value="item.value">
              <template>
                <div class="select-style" :style="{'background': item.value}"></div>
              </template>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addTagFn">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改标签 -->
    <el-dialog title="修改标签" :visible.sync="editDialogVisible" width="50%">
      <el-form label-position="right" :rules="comRules" ref="comFormRef" label-width="80px" :model="addTagForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="addTagForm.name"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="color">
          <el-select v-model="addTagForm.color" placeholder="请选择标签颜色">
            <el-option v-for="item in colorlist" :key="item.value" :label="item.label" :value="item.value">
              <template>
                <div class="select-style" :style="{'background': item.value}"></div>
              </template>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addTagFn">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
    findTagsApi,
    deleteTagApi,
    editTagApi,
    addTagApi
  } from '@/api/type'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8
        },
        total: 0,
        taglist: [],
        addDialogVisible: false,
        editDialogVisible: false,
        comRules: {
          name: [{
            required: true,
            message: '请输入分类名称！',
            trigger: 'blur'
          }],
          color: [{
            required: true,
            message: '请输入选择颜色！',
            trigger: 'blur'
          }]
        },
        addTagForm: {
          name: '',
          color: ''
        },
        colorlist: [{
          value: '#ff9999',
          label: '粉色1'
        }, {
          value: '#ff696c',
          label: '粉色2'
        }, {
          value: '#009cff',
          label: '蓝色1'
        }, {
          value: '#4fc277',
          label: '绿色1'
        }, {
          value: '#009999',
          label: '绿色2'
        }, {
          value: '#c68bf9',
          label: '紫色1'
        }, {
          value: '#ffcc99',
          label: '黄色1'
        }, {
          value: '#5abef6',
          label: '蓝色2'
        }]
      }
    },
    created() {
      this.getTagList()
    },
    methods: {
      async getTagList() {
        const {
          data
        } = await findTagsApi(this.queryInfo)
        this.taglist = data.data.tags.rows
        this.total = data.data.tags.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getTagList()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getTagList()
      },
      // 点击删除按钮，删除留言
      async deleteTagById(id) {
        const res = await this.$confirm('此操作将永久删除该标签及 !!! 属于该分类的文章 !!!, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch((err) => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const {
          data
        } = await deleteTagApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)

        this.getTagList()
      },
      async addTagFn() {
        this.$refs.addFormRef.validate(async valid => {
          if (!valid) return;
          const { data } = await addTagApi(this.addTagForm)
          if (!data.success) return this.$message.error(data.message)
          this.$message.success(data.message)
          this.addDialogVisible = false
          this.getTagList()
          this.$refs.addFormRef.resetFields()
        })
      }
    }
  }
</script>

<style lang="less" scoped>
  .tag-color {
    width: 100%;
    height: 10px;
  }
  .el-select-dropdown__item {
    display: flex;
    align-items: center;
  }
  .select-style {
    width: 80px;
    height: 60%;
  }
</style>