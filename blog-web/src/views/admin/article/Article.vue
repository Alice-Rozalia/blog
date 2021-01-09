<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>文章管理</el-breadcrumb-item>
      <el-breadcrumb-item>文章列表</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-table :data="articleList" border stripe>
        <el-table-column type="index" width="30"></el-table-column>
        <el-table-column prop="title" label="标题" align="center"></el-table-column>
        <el-table-column prop="categoryName" label="分类" align="center"></el-table-column>
        <el-table-column prop="author.nickname" label="作者" align="center"></el-table-column>
        <el-table-column prop="color" label="封面" align="center">
          <template slot-scope="scope">
            <img :src="scope.row.cover" style="width: 90px" />
          </template>
        </el-table-column>
        <el-table-column label="发布" align="center" width="70">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.published" active-color="#13ce66" />
          </template>
        </el-table-column>
        <el-table-column label="是否为转载" align="center" width="90">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.shareStatement" active-color="#13ce66" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">{{scope.row.createTime | dateFormat}}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="goEditArticlePage(scope.row.id)" />
            <el-button size="mini" type="danger" @click="delArticleById(scope.row.id)" icon="el-icon-delete" />
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
    findArticlesApi,
    deleteArticleApi
  } from '@/api/article'
  export default {
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 8
        },
        articleList: [],
        total: 0
      }
    },
    created() {
      this.getArticles()
    },
    methods: {
      async getArticles() {
        const { data } = await findArticlesApi(this.queryInfo)
        this.articleList = data.data.article.rows
        this.total = data.data.article.total
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getArticles()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getArticles()
      },
      async delArticleById(id) {
        const res = await this.$confirm('此操作将永久删除该友情链接, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch((err) => err)

        if (res !== 'confirm') {
          return this.$message.info('删除操作已取消！')
        }

        const { data } = await deleteArticleApi(id)
        if (!data.success) return this.$message.error(data.message)
        this.$message.success("删除成功！")
        this.getArticles()
      },
      goEditArticlePage(id) {
        window.sessionStorage.setItem('activePath', '/admin/publish')
        this.$router.push({
          path: '/admin/publish',
          query: {
            id: id
          }
        })
      }
    }
  }
</script>

<style lang="less" scoped>

</style>