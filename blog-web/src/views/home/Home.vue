<template>
  <!-- 大背景 -->
  <div class="home-bg">
    <div class="entry">
      <h1>抬头看吧，看那天上的繁星！</h1>
      <el-button type="primary" size="mini" @click="goIndex">前往首页</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        drawer: false
      }
    },
    methods: {
      goIndex() {
        this.$router.push('/index')
      }
    }
  }
</script>

<style lang="less" scoped>
  .home-bg {
    position: relative;
    width: 100%;
    height: 100vh;
    background: url(https://kuro-blog.oss-cn-guangzhou.aliyuncs.com/blog-img/home_bg.png) no-repeat fixed center;
    background-size: cover;

    .entry {
      position: absolute;
      width: 100%;
      top: 50%;
      transform: translateY(-70%);
      text-align: center;

      h1 {
        color: #fff;
        font-weight: 400;
        font-family: '华文行楷';
        margin-bottom: 12px;
      }
    }
  }
</style>