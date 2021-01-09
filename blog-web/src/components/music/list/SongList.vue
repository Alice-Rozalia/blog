<template>
  <div class="song-list-container">
    <div class="song-list" v-for="(item, i) in result" :key="i">
      <div class="playCount"><i class="el-icon-caret-right">{{item.playCount | million}}</i></div>
      <img :src="item.picUrl" @click="goPlaylistDetail(item.id)" @mouseover="showPlayIcon(i)" @mouseleave="LeaveConceal" />
      <p class="name">{{item.name}}</p>
      <i class="el-icon-video-play play-video" :style="peg === i ? 'opacity: 1' : 'opacity: 0'"></i>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['result'],
    data() {
      return {
        playIcon: '',
        peg: 10086
      }
    },
    methods: {
      showPlayIcon(index) {
        this.peg = index
      },
      LeaveConceal() {
        this.peg = 10086
      },
      goPlaylistDetail(id) {
        this.$router.push('/music/song_list/' + id)
      }
    }
  }
</script>

<style lang="less" scoped>
  .song-list-container {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    .song-list {
      position: relative;
      width: 140px;
      height: 185px;
      margin-bottom: 20px;

      .playCount {
        position: absolute;
        width: 100%;
        height: 20px;
        background: linear-gradient(rgba(0, 0, 0, 0.27), rgba(0, 0, 0, 0));
        background: -moz-linear-gradient(top, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0) 100%);
        background: -o-linear-gradient(top, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0) 100%);
        border-top-left-radius: 6px;
        border-top-right-radius: 6px;

        >i {
          position: absolute;
          top: 5px;
          right: 5px;
          color: #fff;
          font-size: 12px;
        }
      }

      >img {
        width: 100%;
        height: 140px;
        border-radius: 6px;
        cursor: pointer;
      }

      .name {
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        font-size: 14px;
      }

      .play-video {
        position: absolute;
        bottom: 50px;
        right: 10px;
        font-size: 22px;
        color: #00a4ff;
        opacity: 0;
        transition: all .7s;
      }
    }
  }
</style>