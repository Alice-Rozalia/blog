<template>
  <div>
    <!-- 顶部导航栏 -->
    <Header />

    <div class="resourceContainer">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>资源分享</span></el-breadcrumb-item>
      </el-breadcrumb>

      <el-tabs type="border-card">
        <el-tab-pane label="全部">
          <ul class="cardItem">
            <li v-for="item in resources" :key="item.id">
              <el-card>
                <img :src="item.image" />
                <h3>{{item.name}}</h3>
                <p>提取码：{{item.code}}</p>
                <div class="resourceInfo">
                  <i class="el-icon-collection-tag"><span>{{item.type}}</span></i>
                  <i class="el-icon-user-solid"><span>Kuro</span></i>
                </div>
                <div style="text-align: center;">
                  <el-button icon="el-icon-download" round type="primary" size="mini">
                    <a :href="item.link" style="color: #fff; font-size: 12px" target="_blank">百度网盘</a>
                  </el-button>
                </div>
              </el-card>
            </li>
          </ul>
        </el-tab-pane>
        <el-tab-pane label="常用工具">用户管理</el-tab-pane>
        <el-tab-pane label="视频教程">配置管理</el-tab-pane>
        <el-tab-pane label="电子书">角色管理</el-tab-pane>
        <el-tab-pane label="常用网站">定时任务补偿</el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import { findResourceApi } from '@/api/link'
  export default {
    components: {
      Header
    },
    data() {
      return {
        resources: []
      }
    },
    created() {
      this.getResource()
    },
    methods: {
      async getResource() {
        const { data } = await findResourceApi()
        this.resources = data.data.resources.rows
      }
    }
  }
</script>

<style lang="less">
  .cardItem {
    .el-card__body {
      padding: 10px;
    }
  }
</style>

<style lang="less" scoped>
  li {
    list-style: none;
  }

  .resourceContainer {
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

    .cardItem {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;

      >li {
        width: 280px;
        margin-bottom: 12px;

        img {
          width: 100%;
          height: 150px;
        }

        h3 {
          font-size: 16px;
          text-align: center;
          margin-top: 8px;
          color: #5abef6;
        }

        p {
          font-size: 12px;
          text-align: center;
          color: #808080;
          margin: 5px 0;
        }

        .resourceInfo {
          display: flex;
          justify-content: space-between;
          font-size: 12px;
          color: #333;
          padding-bottom: 3px;
          margin-bottom: 5px;
          border-bottom: 1px solid #ddd;

          span {
            margin-left: 3px;
          }
        }
      }
    }
  }
</style>