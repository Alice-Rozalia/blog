<template>
  <div class="new-song-container">
    <ul>
      <li v-for="item in songs" :key="item.id">
        <div class="play-icon-img">
          <i class="el-icon-video-play"></i>
          <img :src="item.picUrl" />
        </div>
        <div class="intro">
          <p>{{item.name}}</p>
          <div>
            <span class="sq">SQ</span>
            <a href="javascript:;" class="artists" v-for="artist in item.song.artists" :key="artist.id">{{artist.name}}</a>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  import { newSong } from '@/api/music/findMusic/findMusicPageApi'
  export default {
    data() {
      return {
        songs: []
      }
    },
    created() {
      this.getNewSong()
    },
    methods: {
      async getNewSong() {
        const {
          data
        } = await newSong()
        this.songs = data.result.slice(0, 9)
      }
    }
  }
</script>

<style lang="less" scoped>
  .new-song-container {
    height: 200px;
    margin-bottom: 12px;

    >ul {
      display: flex;
      flex-direction: column;
      flex-wrap: wrap;
      justify-content: space-between;
      height: 100%;

      li {
        display: flex;
        width: 300px;
        height: 50px;
        margin-bottom: 12px;
        border-radius: 3px;

        &:hover {
          background: #eaeaea;
        }

        .play-icon-img {
          position: relative;
          width: 50px;

          >i {
            position: absolute;
            top: 50%;
            left: 13px;
            transform: translateY(-50%);
            font-size: 24px;
            color: #00a4ff;
          }

          >img {
            width: 100%;
            border-radius: 3px;
          }
        }

        .intro {
          padding-left: 12px;
          >p {
            color: #373737;
            font-size: 14px;
            padding-bottom: 5px;
          }

          .sq {
            color: #00a4ff;
            border: 1px solid #00a4ff;
            font-size: 10px;
            padding: 0 2px;
            border-radius: 3px;
          }
          .artists {
            color: #9f9f9f;
            font-size: 12px;
            padding-left: 8px;
          }
        }
      }
    }
  }
</style>