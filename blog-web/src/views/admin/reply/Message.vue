<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>留言评论</el-breadcrumb-item>
      <el-breadcrumb-item>留言列表</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-table :data="messageList" border stripe>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="content" label="留言内容" align="center"></el-table-column>
        <el-table-column label="留言时间" align="center">
          <template slot-scope="scope">{{ scope.row.time | dateFormat }}</template>
        </el-table-column>
        <el-table-column label="留言层级" align="center">
          <template slot-scope="scope">{{ scope.row.level + 1 }}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <!-- 删除按钮 -->
            <el-button size="mini" @click="deleteMessageById(scope.row.id)" type="danger" icon="el-icon-delete">删除
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
  import { deleteMessageByIdApi, findMessageListApi } from '@/api/comment'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8
        },
        messageList: [],
        total: 0
      }
    },
    created() {
      this.getMessageList()
    },
    methods: {
      async getMessageList() {
        const { data } = await findMessageListApi(this.queryInfo)
        this.messageList = data.data.message_list.rows
        this.total = data.data.message_list.total
      },
      // 监听页码显示条数改变事件
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getMessageList()
      },
      // 监听页码值改变事件
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getMessageList()
      },
      async deleteMessageById(id) {
        const res = await this.$confirm('此操作将永久删除该留言（包括上下级）, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch(err => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const { data } = await deleteMessageByIdApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.getMessageList()
      }
    }
  }
</script>

<style lang="less" scoped>

</style>