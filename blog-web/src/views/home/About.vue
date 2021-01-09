<template>
  <div class="about-limiting-surface">
    <Header />

    <div class="about-container">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i></el-breadcrumb-item>
        <el-breadcrumb-item><span>关于</span></el-breadcrumb-item>
      </el-breadcrumb>

      <el-card>
        <el-tabs v-model="activeName" stretch>
          <el-tab-pane label="关于博客" name="first">
            <div class="about-intro">
              <img src="https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/avatar.jpg" />
              <h2>稻荷的博客</h2>
              <p>前端小白个人博客，记录一些文章及学习中的代码</p>
            </div>
            <el-divider></el-divider>
            <el-divider><i class="el-icon-info"></i> 简介</el-divider>
            <div class="about-blog">
              <p>该博客是由个人开发的博客网站，前端框架使用Vue2.6.11 + Element-UI，</p>
              <p>后端采用SpringBoot + Shiro + Mybatis + Mysql + Redis</p>
              <p>于2020年10月底开始开发</p>
              <p>本站采用阿里云提供的服务器ECS和阿里云的对象存储OSS。</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="关于作者" name="second">

          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  export default {
    components: {
      Header
    },
    data() {
      return {
        activeName: 'first'
      }
    }
  }
</script>

<style lang="less" scoped>
  .about-limiting-surface {
    height: 100%;
    background: url(https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/bg_body.jpg) repeat;
  }

  .about-container {
    width: 1200px;
    margin: 0 auto;

    .el-breadcrumb {
      height: 29px;
      line-height: 29px;
      padding: 20px 0 0 0;

      >span {
        font-size: 12px;
      }
    }

    .about-intro {
      display: flex;
      flex-direction: column;
      align-items: center;

      >img {
        width: 200px;
        border-radius: 50%;
      }

      >h2 {
        font-size: 26px;
        font-family: KaiTi;
        height: 70px;
        line-height: 70px;
      }

      >p {
        font-size: 16px;
        color: #333;
      }
    }

    .about-blog {
      font-size: 14px;
      text-align: center;
      color: #333;

      >p {
        height: 26px;
        line-height: 26px;
      }
    }
  }
</style>