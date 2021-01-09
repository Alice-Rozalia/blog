<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="6">
          <span class="select-name">昵称</span>
          <el-input v-model="queryInfo.key" placeholder="请输入昵称查询" size="small" style="width: 70%"></el-input>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" size="small" icon="el-icon-search">查询</el-button>
          <el-button type="success" size="small" icon="el-icon-plus">添加</el-button>
          <el-button type="warning" size="small" icon="el-icon-download">导出</el-button>
        </el-col>
      </el-row>

      <!-- 用户列表区域 -->
      <el-table :data="userlist" border stripe>
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column label="头像" align="center">
          <template slot-scope="scope">
            <img :src="scope.row.avatar" width="50px" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间">
          <template slot-scope="scope">{{scope.row.createTime | dateFormat}}</template>
        </el-table-column>
        <el-table-column label="最后修改时间">
          <template slot-scope="scope">{{scope.row.modifiedTime === null ? '暂无修改' : (scope.row.modifiedTime | dateFormat)}}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" active-color="#13ce66" @change="userStateChanged(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="deleteTagById(scope.row.id)">
            </el-button>
            <!-- 删除按钮 -->
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteTagById(scope.row.id)">
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
  </div>
</template>

<script>
  import {
    findUserListApi,
    userChangeStatusApi
  } from '@/api/users'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8,
          key: ''
        },
        total: 0,
        userlist: []
      }
    },
    created() {
      this.getUserList()
    },
    methods: {
      async getUserList() {
        const {
          data
        } = await findUserListApi(this.queryInfo)
        this.userlist = data.data.users.rows
        this.total = data.data.users.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getUserList()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getUserList()
      },
      // 点击删除按钮，删除留言
      async deleteTagById(id) {
        const res = await this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch((err) => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const {
          data
        } = await this.$http.delete('/item/comment/remove/' + id)
        if (data.status !== 200) return this.$message.error(data.msg)
        this.$message.success(data.msg)

        this.getUserList()
      },
      async userStateChanged(val) {
        const { data } = await userChangeStatusApi(val.id, val.status)
        console.log(data);
      }
    }
  }
</script>

<style lang="less" scoped>
  .select-name {
    font-size: 14px;
    padding: 0 12px;
  }
</style>