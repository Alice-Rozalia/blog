<template>
  <div class="diary">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日记管理</el-breadcrumb-item>
      <el-breadcrumb-item>日记列表</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-timeline>
        <el-timeline-item v-for="item in diarylist" :key="item.id" :timestamp="item.createTime | dateFormat"
          color="#31c27c" placement="top">
          <el-card shadow="hover">
            <i class="el-icon-close close" @click="deleteDiaryById(item.id)"></i>
            <i class="el-icon-edit edit" @click="goEditPage(item.id)"></i>
            <div v-html="item.content"></div>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <!-- 分页区域 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryInfo.page" :page-sizes="[2, 4, 6, 8]" :page-size="queryInfo.rows"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
  import {
    findDiarysApi,
    deleteDiaryApi
  } from '@/api/diary'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 4
        },
        total: 0,
        diarylist: []
      }
    },
    created() {
      this.getDiarys()
    },
    methods: {
      async getDiarys() {
        const { data } = await findDiarysApi(this.queryInfo)
        this.diarylist = data.data.diarys.rows
        this.total = data.data.diarys.total
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getDiarys()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getDiarys()
      },
      async deleteDiaryById(id) {
        const res = await this.$confirm('此操作将永久删除该日记记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch(err => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const {
          data
        } = await deleteDiaryApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success(data.message)
        this.getDiarys()
      },
      goEditPage(id) {
        window.sessionStorage.setItem('activePath', '/admin/keep_diarys')
        this.$router.push({
          path: '/admin/keep_diarys',
          query: {
            id: id
          }
        })
      }
    }
  }
</script>

<style lang="less">
  .diary {
    .el-card__body {
      position: relative;
    }
  }
</style>

<style lang="less" scoped>
  .close {
    position: absolute;
    right: 5px;
    top: 5px;
    cursor: pointer;
    font-size: 16px;
  }

  .edit {
    position: absolute;
    right: 35px;
    top: 5px;
    cursor: pointer;
    font-size: 14px;
  }
</style>