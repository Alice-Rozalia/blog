<template>
  <div class="find-music-container scrollbar">
    <el-tabs v-model="activeName">
      <el-tab-pane label="个性推荐" name="1">
        <Swiper />
        <h3 style="padding-bottom: 13px">推荐歌单<i class="el-icon-arrow-right"></i></h3>
        <Song-List :result='songList' />
        <h3 style="padding-bottom: 13px">独家放送<i class="el-icon-arrow-right"></i></h3>
        <Exclusive />
        <h3 style="padding-bottom: 13px">最新音乐<i class="el-icon-arrow-right"></i></h3>
        <New-Song />
        <h3 style="padding-bottom: 13px">推荐MV<i class="el-icon-arrow-right"></i></h3>
        <Personalized-Mv />
        <h3 style="padding-bottom: 13px">主播电台<i class="el-icon-arrow-right"></i></h3>
        <Radio />
      </el-tab-pane>
      <el-tab-pane label="歌单" name="2">配置管理</el-tab-pane>
      <el-tab-pane label="主播电台" name="3">角色管理</el-tab-pane>
      <el-tab-pane label="排行榜" name="4">定时任务补偿</el-tab-pane>
      <el-tab-pane label="歌手" name="5">定时任务补偿</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import Swiper from '@/components/music/swiper/Swiper.vue'
  import SongList from '@/components/music/list/SongList.vue'
  import Exclusive from '@/components/music/list/Exclusive.vue'
  import NewSong from '@/components/music/list/NewSong.vue'
  import PersonalizedMv from '@/components/music/list/PersonalizedMV.vue'
  import Radio from '@/components/music/list/Radio.vue'
  import { Personalized } from '@/api/music/findMusic/findMusicPageApi'
  export default {
    components: {
      Swiper,
      SongList,
      Exclusive,
      NewSong,
      PersonalizedMv,
      Radio
    },
    data() {
      return {
        activeName: '1',
        songList: []
      }
    },
    created() {
      this.getSongList()
    },
    methods: {
      async getSongList() {
        const {
          data
        } = await Personalized()
        this.songList = data.result
        // console.log(data);
      }
    }
  }
</script>

<style lang="less">
  .find-music-container {
    .el-tabs__nav-wrap::after {
      width: 0;
    }

    .el-tabs__item {
      font-size: 15px;
    }

    .el-tabs__item.is-active {
      font-weight: 700;
      font-size: 17px;
    }
  }
</style>

<style lang="less" scoped>
  @import '@/style/music/scrollbar.less';

  li {
    list-style: none;
  }
  .find-music-container {
    padding: 10px 30px;
  }
</style>