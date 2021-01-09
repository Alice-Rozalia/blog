<template>
  <div>
    <!-- 顶部导航栏 -->
    <Header />

    <div class="music-container">
      <el-container>
        <el-header style="padding: 0">
          <Music-Head />
        </el-header>
        <el-main style="padding: 0">
          <el-container>
            <el-aside width="200px">
              <Music-Aside />
            </el-aside>
            <el-main style="padding: 0">
              <router-view/>
            </el-main>
          </el-container>
        </el-main>
        <el-footer style="padding: 0">
          <Music-Footer />
        </el-footer>
      </el-container>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'
  import MusicHead from '@/components/music/MusicHead.vue'
  import MusicAside from '@/components/music/MusicAside.vue'
  import MusicFooter from '@/components/music/MusicFooter.vue'
  export default {
    components: {
      Header,
      MusicHead,
      MusicAside,
      MusicFooter
    }
  }
</script>

<style lang="less" scoped>
  li {
    list-style: none;
  }

  .music-container {
    width: 1200px;
    margin: 20px auto 0;
    box-shadow: 0 12px 12px 0 rgba(0, 0, 0, 0.2);
  }
</style>