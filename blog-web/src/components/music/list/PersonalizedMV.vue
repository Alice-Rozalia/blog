<template>
  <div class="personalized-mv-container">
    <div class="personalized-list" @mouseover="showName(i)" @mouseleave="LeaveConceal" v-for="(item, i) in list" :key="item.id">
      <div class="play-count" v-show="i !== peg">
        <i class="el-icon-caret-right">{{item.playCount}}</i>
      </div>
      <div class="copywriter">{{item.copywriter}}</div>
      <img :src="item.picUrl" />
      <p><a href="javascript:;" class="mv-name">{{item.name}}</a></p>
      <p>
        <a href="javascript:;" class="artist" v-for="artist in item.artists" :key="artist.id">
          {{artist.name}}
        </a>
      </p>
    </div>
  </div>
</template>

<script>
  import { personalizedMV } from '@/api/music/findMusic/findMusicPageApi'
  export default {
    data() {
      return {
        list: [],
        peg: 999
      }
    },
    created() {
      this.getPersonalizedMV()
    },
    methods: {
      async getPersonalizedMV() {
        const { data } = await personalizedMV()
        this.list = data.result.slice(0, 3)
      },
      showName(index) {
        this.peg = index
      },
      LeaveConceal() {
        this.peg = 999
      }
    }
  }
</script>

<style lang="less" scoped>
  .personalized-mv-container {
    display: flex;
    justify-content: space-between;
    margin-bottom: 30px;

    .personalized-list {
      position: relative;
      width: 245px;
      height: 190px;
      overflow: hidden;

      &:hover .copywriter {
        transform: translateY(30px);
      }

      .play-count {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        color: #fff;
        font-size: 13px;

        >i {
          position: absolute;
          top: 5px;
          right: 6px;
        }
      }

      .copywriter {
        position: absolute;
        top: -30px;
        left: 0;
        width: 100%;
        height: 30px;
        box-sizing: border-box;
        line-height: 30px;
        font-size: 13px;
        padding-left: 6px;
        border-radius: 5px;
        background: rgba(0, 0, 0, 0.3);
        cursor: pointer;
        transition: all 0.5s;
        color: #fff;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      >img {
        width: 100%;
        border-radius: 5px;
        cursor: pointer;
      }

      .mv-name {
        display: block;
        font-size: 15px;
        padding: 5px 0;
      }

      .artist {
        font-size: 13px;
        color: #999;
      }
    }
  }
</style>