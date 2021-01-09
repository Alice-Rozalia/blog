<template>
  <div class="radio-container">
    <div class="radio-list" v-for="item in toplist" :key="item.id">
      <div class="head">
        <img :src="item.picUrl" />
        <div class="name">{{item.name}}</div>
      </div>
      <p class="signature">
        <a href="javascript:;">{{item.dj.signature}}</a>
      </p>
    </div>
  </div>
</template>

<script>
  import { Radio } from '@/api/music/findMusic/findMusicPageApi'
  export default {
    data() {
      return {
        toplist: []
      }
    },
    created() {
      this.getRadio()
    },
    methods: {
      async getRadio() {
        const { data } = await Radio()
        this.toplist = data.toplist
      }
    }
  }
</script>

<style lang="less" scoped>
  .radio-container {
    display: flex;
    justify-content: space-between;

    .radio-list {
      width: 140px;
      height: 190px;

      .head {
        position: relative;
        height: 140px;

        >img {
          width: 100%;
          border-radius: 5px;
        }

        .name {
          position: absolute;
          bottom: 0;
          width: 100%;
          height: 30px;
          line-height: 30px;
          font-size: 14px;
          padding-left: 6px;
          color: #fff;
          box-sizing: border-box;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          background: linear-gradient(rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.35));
          background: -moz-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.35) 100%);
          background: -o-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.35) 100%);
          border-bottom-left-radius: 5px;
          border-bottom-right-radius: 5px;
        }
      }

      .signature {
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        padding-top: 8px;

        >a {
          font-size: 14px;

          &:hover {
            color: #000;
          }
        }
      }
    }
  }
</style>