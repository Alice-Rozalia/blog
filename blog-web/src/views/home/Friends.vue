<template>
  <div>
    <!-- 顶部导航栏 -->
    <Header />

    <div class="friendContainer">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>友人帐</span></el-breadcrumb-item>
      </el-breadcrumb>

      <el-card>
        <el-divider><i class="el-icon-connection">互换友链</i></el-divider>
        <section>
          <waterfall :col='col' :data="links" :width="itemWidth" :gutterWidth="gutterWidth">
            <template>
              <div class="card" v-for="item in links" :key="item.id">
                <a class="link-a" :href="item.link">
                  <img style="vertical-align:top" :lazy-src="item.image" width="100%" height="100%" />
                </a>
                <p class="link-name">{{item.name}}</p>
              </div>
              <div class="card" v-for="item in links" :key="item.id">
                <a :href="item.link">
                  <img style="vertical-align:top" :lazy-src="item.image" width="100%" height="100%" />
                </a>
                <p class="link-name">{{item.name}}</p>
              </div>
            </template>
          </waterfall>
        </section>
      </el-card>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import {
    findFriendlyLinkApi
  } from '@/api/link'
  export default {
    components: {
      Header
    },
    data() {
      return {
        links: [],
        total: 0,
        col: 4,
        itemWidth: 224,
        gutterWidth: 20
      }
    },
    created() {
      this.getFriendlyLink()
    },
    methods: {
      async getFriendlyLink() {
        const {
          data
        } = await findFriendlyLinkApi()
        this.links = data.data.links.rows
        this.total = data.data.links.total
        console.log(this.links);
      }
    }
  }
</script>

<style lang="less" scoped>
  .friendContainer {
    width: 1000px;
    margin: 0 auto;

    .el-breadcrumb {
      height: 29px;
      line-height: 29px;
      padding: 20px 0;

      >span {
        font-size: 12px;
      }
    }

    .card {
      margin-bottom: 15px;
      border-radius: 8px;
      overflow: hidden;

      .link-name {
        text-align: center;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        font-family: STSong;
        font-size: 16px;
        background: #00a4ff;
        padding: 5px 0;
        color: #fff;
      }
    }
  }
</style>