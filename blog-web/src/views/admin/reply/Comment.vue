<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>留言评论</el-breadcrumb-item>
      <el-breadcrumb-item>评论列表</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-table :data="commentList" border stripe>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="content" label="评论内容" align="center"></el-table-column>
        <el-table-column label="评论时间" align="center">
          <template slot-scope="scope">{{ scope.row.commentTime | dateFormat }}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <!-- 删除按钮 -->
            <el-button size="mini" @click="deleteCommentById(scope.row.id)" type="danger" icon="el-icon-delete">删除
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
    deleteCommentByIdApi,
    findCommentListApi
  } from '@/api/comment'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8
        },
        commentList: [],
        total: 0
      }
    },
    created() {
      this.getCommentList()
    },
    methods: {
      async getCommentList() {
        const { data } = await findCommentListApi()
        this.commentList = data.data.comment_list.rows
        this.total = data.data.comment_list.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getCommentList()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getCommentList()
      },
      async deleteCommentById(id) {
        const res = await this.$confirm('此操作将永久删除该留言（包括上下级）, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch(err => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const { data } = await deleteCommentByIdApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.getCommentList()
      }
    }
  }
</script>

<style lang="less" scoped>

</style>