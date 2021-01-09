<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志系统</el-breadcrumb-item>
      <el-breadcrumb-item>登录日志</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- 登录日志列表区域 -->
      <el-table :data="logs" border stripe>
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="nickname" label="登陆昵称" align="center"></el-table-column>
        <el-table-column prop="location" label="地点" align="center"></el-table-column>
        <el-table-column label="登录时间" align="center">
          <template slot-scope="scope">{{scope.row.loginTime | dateFormat}}</template>
        </el-table-column>
        <el-table-column prop="ip" label="ip地址" align="center"></el-table-column>
        <el-table-column prop="userSystem" label="操作系统" align="center" width="110"></el-table-column>
        <el-table-column prop="userBrowser" label="浏览器" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="90">
          <template slot-scope="scope">
            <!-- 删除按钮 -->
            <el-button size="mini" @click="deleteLoginLogById(scope.row.id)" type="danger" icon="el-icon-delete" />
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
    findLoginLogsApi,
    deleteLoginLogByIdApi
  } from '@/api/log'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8
        },
        logs: [],
        total: null
      }
    },
    created() {
      this.getLoginLog()
    },
    methods: {
      async getLoginLog() {
        const {
          data
        } = await findLoginLogsApi(this.queryInfo)
        this.logs = data.data.loginLogs.rows
        this.total = data.data.loginLogs.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getLoginLog()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getLoginLog()
      },
      async deleteLoginLogById(id) {
        const res = await this.$confirm('此操作将永久删除该登录日志, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch(err => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const { data } = await deleteLoginLogByIdApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.getLoginLog()
      }
    }
  }
</script>

<style lang="less" scoped>

</style>