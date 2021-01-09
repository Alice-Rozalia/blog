<template>
  <div>
    <!-- 顶部导航栏 -->
    <Header />

    <div class="mainContainer">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>文章专栏</span></el-breadcrumb-item>
      </el-breadcrumb>

      <div style="display: flex; justify-content: space-between;">
        <!-- 文章列表区域 -->
        <div class="leftArticles">
          <div v-for="item in $store.state.articles" :key="item.id">
            <Article-Item :article="item" />
          </div>

          <!-- 分页组件 -->
          <div class="pageComponent">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="queryInfo.page" :page-sizes="[3, 5, 8, 10]" :page-size="queryInfo.rows"
              layout="total, sizes, prev, pager, next, jumper" :total="$store.state.total" background>
            </el-pagination>
          </div>
        </div>

        <!-- 右侧小栏目 -->
        <Aside />
      </div>
    </div>
    
    <!-- 页脚 -->
    <Footer />
    <el-backtop />
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import Footer from '@/components/Footer.vue'
  import ArticleItem from '@/components/ArticleItem.vue'
  import Aside from '@/components/Aside.vue'
  import { findArticlesApi } from '@/api/article'

  export default {
    components: {
      Header,
      ArticleItem,
      Aside,
      Footer
    },
    data() {
      return {
        queryInfo: {
          page: 1,
          rows: 5
        }
      }
    },
    created() {
      this.getArticleList()
    },
    methods: {
      async getArticleList() {
        const { data } = await findArticlesApi(this.queryInfo)
        this.$store.dispatch('setArticle', data.data.article.rows)
        this.$store.dispatch('setTotal', data.data.article.total)
      },
      handleSizeChange(newSize) {
        this.queryInfo.rows = newSize
        this.getArticleList()
      },
      handleCurrentChange(newPage) {
        this.queryInfo.page = newPage
        this.getArticleList()
      }
    }
  }
</script>

<style lang="less" scoped>
  .mainContainer {
    width: 1200px;
    margin: 0 auto;

    .el-breadcrumb {
      height: 29px;
      line-height: 29px;
      padding: 20px 0;

      >span {
        font-size: 12px;
      }
    }

    .leftArticles {
      width: 890px;

      .pageComponent {
        text-align: center;
      }
    }
  }

  .el-card {
    margin-bottom: 12px;
  }
</style>